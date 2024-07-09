package Application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class Calculator extends Application {

    private TextField display;
    private Double num1, num2;
    private String operator;
    private boolean startNewNumber = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = create();
        Scene scene = new Scene(root, 400, 500);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyRelease);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Calculator");
        primaryStage.show();
    }

    public Parent create() {
        GridPane gridPane = createGridPane();

        // Create display
        display = new TextField();
        display.setEditable(false);
        display.setStyle("-fx-font-size: 24; -fx-text-fill: white; -fx-background-color: #2A2828; -fx-border-color: #2A2828;");
        display.setMaxWidth(Double.MAX_VALUE);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setPadding(new Insets(10));

        // Add display to gridPane
        GridPane.setConstraints(display, 0, 0, 4, 1);
        gridPane.getChildren().add(display);

        // Add number buttons and other buttons
        addButtons(gridPane);

        return gridPane;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();

        // Define columns and rows
        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            gridPane.getColumnConstraints().add(col);
        }

        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(16.67);
            gridPane.getRowConstraints().add(row);
        }

        gridPane.setStyle("-fx-background-color: #2A2828;");
        gridPane.setPadding(new Insets(0));
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        return gridPane;
    }

    private void addButtons(GridPane gridPane) {
        String[][] buttonLabels = {
            {"AC", "+/-", "%", "÷"},
            {"7", "8", "9", "*"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"0", ".", "="}
        };

        EventHandler<ActionEvent> handler = this::handleButtonAction;

        for (int row = 0; row < buttonLabels.length; row++) {
            for (int col = 0; col < buttonLabels[row].length; col++) {
                String text = buttonLabels[row][col];
                Button button = new Button(text);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setOnAction(handler);

                // Set initial style based on button type
                if (row == 0 && col < 3) {
                    button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #505050; -fx-border-color: #2A2828;");
                } else if (col == 3) {
                    button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #EB5935; -fx-border-color: #2A2828;");
                } else {
                    button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #696969; -fx-border-color: #2A2828;");
                }

                // Apply dim effect on mouse press
                final int finalRow = row; // effectively final variable for lambda expression
                final int finalCol = col; // effectively final variable for lambda expression
                button.setOnMousePressed(e -> {
                    button.setStyle(button.getStyle() + "; -fx-opacity: 0.6;");
                    handleInput(button.getText(), finalRow, finalCol);
                });
                                
                button.setOnMouseReleased(e -> {
                	if (finalRow == 0 && finalCol < 3) {
                        button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #505050; -fx-border-color: #2A2828;");
                    } else if (finalCol == 3) {
                        button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #EB5935; -fx-border-color: #2A2828;");
                    } else {
                        button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #696969; -fx-border-color: #2A2828;");
                    }
                });

                // Adjust button layout based on text
                if (text.equals("0")) {
                    GridPane.setConstraints(button, 0, row + 1, 2, 1);
                } else if (text.equals(".")) {
                    GridPane.setConstraints(button, 2, row + 1);
                } else if (text.equals("=")) {
                    GridPane.setConstraints(button, 3, row + 1);
                } else {
                    GridPane.setConstraints(button, col, row + 1);
                }

                GridPane.setHalignment(button, HPos.CENTER);
                GridPane.setValignment(button, VPos.CENTER);
                gridPane.getChildren().add(button);
            }
        }
    }

    private void handleButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        // You can handle actions common to all buttons here, if needed
    }

    private void handleInput(String text, int row, int col) {
        // Handle input based on the button text
        if (text.matches("[0-9]") || text.equals(".")) {
            if (startNewNumber) {
                display.setText(text);
                startNewNumber = false;
            } else {
                display.appendText(text);
            }
        } else if (text.equals("AC")) {
            display.clear();
            num1 = num2 = 0.0;
            operator = "";
            startNewNumber = true;
        } else if (text.equals("+/-")) {
            if (!display.getText().isEmpty()) {
                double currentValue = Double.parseDouble(display.getText());
                currentValue *= -1;
                display.setText(String.valueOf(currentValue));
            }
        } else if (text.matches("[÷\\*\\-\\+\\%\\/]")) {
            operator = text;
            if (!display.getText().isEmpty()) {
            	if (num2 == null)
            	{
            		double result = num1;
            		display.setText(String.valueOf(result));
                    num1 = result;
            	}
            	else if (num2 != null)
        		{
            		num1 = Double.parseDouble(display.getText());
        		}
            }
            startNewNumber = true;
        } else if (text.equals("=")) {
            if (!display.getText().isEmpty()) {
                num2 = Double.parseDouble(display.getText());
                double result = 0;
            	switch (operator) 
            	{
                    case "÷":
                        result = num1 / num2;
                        break;
                    case "*":
                        result = num1 * num2;
                        break;
                    case "-":
                        result = num1 - num2;
                        break;
                    case "+":
                        result = num1 + num2;
                        break;
                    case "%":
                        result = num1 % num2;
                        break;
            	}
                display.setText(String.valueOf(result));
                num1 = result;
                startNewNumber = true;
            }
        }
    }

    private void handleKeyPress(KeyEvent event) {
        String text = event.getText();
        KeyCode code = event.getCode();
        boolean ignoreDecimalRepeat = false;
        String buttonText = getButtonTextForKeyCode(code, text, event.isShiftDown());
   
        if (buttonText != null) {
        	if (buttonText.equals(".") && display.getText().contains("."))
        	{
        		ignoreDecimalRepeat = true;
        	}
            for (Button button : getAllButtons()) {
                if (button.getText().equals(buttonText)) {
                    // Reset button style based on type
                    int row = GridPane.getRowIndex(button) - 1; // Adjust row index for 0-based
                    int col = GridPane.getColumnIndex(button);
                    if (row == 0 && col < 3) {
                    	button.setStyle(button.getStyle() + "; -fx-opacity: 0.6;");
                        handleInput(button.getText(), row, col);
                    } else if (col == 3) {
                    	button.setStyle(button.getStyle() + "; -fx-opacity: 0.6;");
                    	handleInput(button.getText(), row, col);
                    } else {
                    	button.setStyle(button.getStyle() + "; -fx-opacity: 0.6;");
                        if (!ignoreDecimalRepeat) handleInput(button.getText(), row, col);
                    }
                    break;
                }
            }
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        KeyCode code = event.getCode();
        String text = event.getText();
        String buttonText = getButtonTextForKeyCode(code, text, event.isShiftDown());
        if (buttonText != null) {
            for (Button button : getAllButtons()) {
                if (button.getText().equals(buttonText)) {
                    // Reset button style based on type
                    int row = GridPane.getRowIndex(button) - 1; // Adjust row index for 0-based
                    int col = GridPane.getColumnIndex(button);
                    if (row == 0 && col < 3) {
                        button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #505050; -fx-border-color: #2A2828;");
                    } else if (col == 3) {
                        button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #EB5935; -fx-border-color: #2A2828;");
                    } else {
                        button.setStyle("-fx-font-size: 20; -fx-text-fill: white; -fx-background-color: #696969; -fx-border-color: #2A2828;");
                    }
                    break;
                }
            }
        }
    }

    private String getButtonTextForKeyCode(KeyCode code, String text, boolean Shift) {
    	if (Shift)
        {
            switch (code) {
                case EQUALS:
                    return "+";
                case DIGIT5:
                    return "%";
                case DIGIT8:
                    return "*";
                default:
                    return null;
            }
        }
    	else
    	{
    		switch (code) 
            {
        		case ENTER:
        			return "=";
        		case EQUALS:
        			return "=";
    	        case SLASH:
    	            return"÷"; // Use -1 to indicate keyboard input
    	        case MINUS:
    	            return("-"); // Use -1 to indicate keyboard input
    	        case BACK_SPACE:
                    return "AC";
    	        default:
                    if (text.matches("[0-9]") || text.equals(".")) {
                        return text;
                    }
                    return null;
            }
    	}
    }

    private Iterable<Button> getAllButtons() {
        GridPane gridPane = (GridPane) display.getParent();
        return (Iterable<Button>) (Object) gridPane.getChildren().filtered(node -> node instanceof Button);
    }
}

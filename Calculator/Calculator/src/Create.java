/*Written by Jared Perlmutter for CS2336.504, assignment 5, starting November 12, 2020.
NetID: JCP190000
This program uses javaFX to display a working basic calculator, with 4 simple functions, 2 inputs for numbers, a clear button, answer button,
and a label the displays the output*/

import javafx.event.*;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class Create extends Calculator{

    //create static doubles
    static double num1, num2;

    //method to create displayed calculator
    static public Parent create()
    {
        //create gridpane
        GridPane gridpane = new GridPane();

        //set gridpane background color
        gridpane.setStyle("-fx-background-color: #2A2828;");

        //create vbox
        VBox root = new VBox();

        //set vbox size
        root.setPrefSize(600, 600);

        //display answer
        Label output = new Label();
        output.setStyle("-fx-text-fill: white; -fx-font-size: 40;");
        output.setMaxWidth(200);

        //set grid positions for output
        GridPane.setRowIndex(output, 1);
        GridPane.setColumnIndex(output, 0);

        //create text fields and variables for numbers
        TextField num1in = new TextField();
        TextField num2in = new TextField();
        num1in.setText("");
        num2in.setText("");
        num1in.setMaxWidth(200);
        num2in.setMaxWidth(200);

        //set colors for textfield backgrounds
        num1in.setStyle("-fx-background-color: #696969; -fx-text-fill: white; -fx-font-size: 27;");
        num2in.setStyle("-fx-background-color: #696969; -fx-text-fill: white; -fx-font-size: 27;");

        //set positions for textfields
        GridPane.setRowIndex(num1in, 0);
        GridPane.setColumnIndex(num1in, 0);
        GridPane.setRowIndex(num2in, 0);
        GridPane.setColumnIndex(num2in, 1);
        num1in.setMinHeight(56);
        num2in.setMinHeight(56);

        //create division button
        Button division = new Button("/");

        //set division button location
        GridPane.setRowIndex(division, 0);
        GridPane.setColumnIndex(division, 4);

        //set colors for division button
        division.setStyle("-fx-text-fill: white; -fx-background-color:#EB5935; -fx-font-size: 27;");
        division.setMinWidth(50);
        division.setMinHeight(50);
        
        //create multiplication button
        Button multiplication = new Button("x");

        //set multiplication button location
        GridPane.setRowIndex(multiplication, 1);
        GridPane.setColumnIndex(multiplication, 4);

        //set colors for multiplication button
        multiplication.setStyle("-fx-text-fill: white; -fx-background-color:#EB5935; -fx-font-size: 27;");
        multiplication.setMinWidth(50);
        multiplication.setMinHeight(50);

        //create subtraction button
        Button subtraction = new Button("━");

        //set subtraction button location
        GridPane.setRowIndex(subtraction, 2);
        GridPane.setColumnIndex(subtraction, 4);

        //set colors for subtraction button
        subtraction.setStyle("-fx-text-fill: white; -fx-background-color:#EB5935; -fx-font-size: 16;");
        subtraction.setMinWidth(50);
        subtraction.setMinHeight(50);
        subtraction.setMaxWidth(50);
        subtraction.setMaxHeight(50);

        //create addition button
        Button addition = new Button("＋");  
        
        //set addition button location
        GridPane.setRowIndex(addition, 3);
        GridPane.setColumnIndex(addition, 4);

        //set colors for addition button
        addition.setStyle("-fx-text-fill: white; -fx-background-color:#EB5935; -fx-font-size: 21;");
        addition.setMinWidth(50);
        addition.setMinHeight(50);

        //clear button
        Button clear = new Button("ce");
        clear.setMinWidth(50);
        clear.setMinHeight(50);
        clear.setMaxWidth(50);
        clear.setMaxHeight(50);
        clear.setStyle("-fx-text-fill: white; -fx-background-color:#EB5935; -fx-font-size: 20;");

        //place clear button
        GridPane.setRowIndex(clear, 4);
        GridPane.setColumnIndex(clear, 4);

        //ans button
        Button ans = new Button("ans");
        ans.setMinWidth(52);
        ans.setMinHeight(50);
        ans.setMaxHeight(50);
        ans.setStyle("-fx-text-fill: white; -fx-background-color:#EB5935; -fx-font-size: 20;");

        //place ans button
        GridPane.setRowIndex(ans, 4);
        GridPane.setColumnIndex(ans, 0);

        //implement clear button
        clear.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent e) 
            {
                //clear the text fields
                num1in.setText("");
                num2in.setText("");

                //assign the text fields to variables
                setNums(num1in, num2in, output);

                //clear output
                output.setText("");
            }
        }); 

        //implement ans button
        ans.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent e) 
            {
                //set the answer to the first text field
                num1in.setText(output.getText());

                //assign the text fields to variables
                setNums(num1in, num2in, output);
            }
        }); 

        //set the output to the result of dividing the two numbers
        division.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent e) 
            {
                //assign the text fields to variables
                setNums(num1in, num2in, output);

                //check if num2 is 0
                if (num2 == 0) output.setText("Error");

                //divide the two numbers and assign to output
                else output.setText(String.valueOf(num1 / num2));
            }
        }); 
        
        //set the output to the result of multiplying the two numbers
        multiplication.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent e) 
            {
                //assign the text fields to variables
                setNums(num1in, num2in, output);

                //multiply the two numbers and assign to output
                output.setText(String.valueOf(num1 * num2));
            }
        });   

        //set the output to the result of subtracting the two numbers
        subtraction.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent e) 
            {
                //assign the text fields to variables
                setNums(num1in, num2in, output);

                //subtract the two numbers and assign to output
                output.setText(String.valueOf(num1 - num2));
            }
        });   

        //set the output to the result of adding the two numbers
        addition.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent e) 
            {
                //assign the text fields to variables
                setNums(num1in, num2in, output);

                //add the two numbers and assign to output
                output.setText(String.valueOf(num1 + num2));
            }
        });   

        //add name
        Text name = new Text("JP 2020");
        name.setFill(Color.WHITE);
        name.setStyle("-fx-font-size: 20;");
        GridPane.setRowIndex(name, 4);
        GridPane.setColumnIndex(name, 1);

        //add elements to display
        gridpane.getChildren().addAll(num1in, num2in, division, multiplication, subtraction, addition, clear, ans, name, output);

        return gridpane;
    }

    //assign the user-input to veriables
    static public void setNums(TextField num1in, TextField num2in, Label output)
    {
        //check if value is a valid number
        try
        {
            num1 = Double.parseDouble(num1in.getText());
            num2 = Double.parseDouble(num2in.getText());  
        }

        //if not, set values to zero
        catch(NumberFormatException e)
        {
            num1 = 0;
            num2 = 0;  
            num1in.setText("");
            num2in.setText("");
        }
        
    }
}
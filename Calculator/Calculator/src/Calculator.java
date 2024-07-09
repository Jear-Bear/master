/*Written by Jared Perlmutter for CS2336.504, assignment 5, starting November 12, 2020.
NetID: JCP190000
This program displays the calculator, as well as sets the window icon and name.*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
 
public class Calculator extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }
 
    @Override
    public void start(Stage stage)
    {
        //create and name new scene
        stage.setScene(new Scene(Create.create()));
        stage.setTitle("Calculator");

        //set icon for new scene and show the scene
        stage.getIcons().add(new Image("https://p.kindpng.com/picc/s/185-1850417_calculator-icon-hd-png-download.png"));
        stage.show();
    }
}
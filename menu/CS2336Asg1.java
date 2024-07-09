import java.util.*;
import java.util.Scanner;
import java.lang.*;

public class CS2336Asg1 
{
    public static void main(String[] arg)
    {
        //userinput values and scanner
        int userInput = 0;
        double userValue;
        Scanner scan = new Scanner(System.in);
        boolean notDone = true;

        while (notDone)
        {
            while (userInput > 7 || userInput < 1)
            {
                //println commands to print out the menu
                System.out.println("Enter a number that corresponds with the choices below:\n");
                System.out.println("[1] Fahrenheit to Celcius\n");
                System.out.println("[2] Celcius to Fahrenheit\n");
                System.out.println("[3] pounds to kilograms\n");
                System.out.println("[4] kilograms to pounds\n");
                System.out.println("[5] miles to kilometers\n");
                System.out.println("[6] kilometers to miles\n");
                System.out.println("[7] Exit the program");
                userInput = scan.nextInt();

                if (userInput > 7 || userInput < 1)
                {
                    System.out.printf("Invalid option\n");
                }
            }

            //check if need to exit
            if (userInput == 7)
            {
                System.out.println("Goodbye!");
                System.exit(0);
            }

            //request value to be converted
            System.out.println("Enter the value you are converting:");
            userValue = scan.nextDouble();
    
            //decide which function to call based on userInput variable
            if (userInput == 1)
            {
                temper far = new temper();
                far.temperature = userValue;
                System.out.printf("%.2f", far.farToCels());
                notDone = false;
            }
    
            else if (userInput == 2)
            {
                temper cel = new temper();
                cel.temperature = userValue;
                System.out.printf("%.2f", cel.celsToFar());
                notDone = false;
            }
    
            else if (userInput == 3)
            {
                weight lbs = new weight();
                lbs.w = userValue;
                System.out.printf("%.2f", lbs.poundsToKilo());
                notDone = false;
            }
    
            else if (userInput == 4)
            {
                weight kgs = new weight();
                kgs.w = userValue;
                System.out.printf("%.2f", kgs.kiloToPounds());
                notDone = false;
            }
    
            else if (userInput == 5)
            {
                dist mil = new dist();
                mil.distance = userValue;
                System.out.printf("%.2f", mil.milesToKilo());
                notDone = false;
            }
    
            else if (userInput == 6)
            {
                dist kilo = new dist();
                kilo.distance = userValue;
                System.out.printf("%.2f", kilo.kiloToMiles());
                notDone = false;
            }
        }    
    }
}
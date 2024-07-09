/*
Name: Jared Perlmutter
ID: JCP 190000
Date: 9/07/20
*/

import java.util.*;
import java.text.*;
public class main
{
    public static void main(String[] args)
    {
        //counter to keep track of location in array of triangles
        int c = 0;

        //create scanner and string to read and store user input
        Scanner scan = new Scanner(System.in);
        String selected = "";

        //create boolean to validate user input
        boolean isValid = false;

        //allocate an array for a maximum of 100 triangles
        Triangle[] triangles = new Triangle[100];
        Triangle[] trianglesCopy;

        //Decimal format for displaying data
        DecimalFormat df = new DecimalFormat("#,###,##0.00");

        do
        {
            if (isValid == false)
            {
                //print menu
                System.out.println("Please select one of the following options:");
                System.out.println("(1) Enter data for a new triangle.");
                System.out.println("(2) Print all triangles sorted by area, smallest to largest.");
                System.out.println("(3) Print all triangles of a specified color.");
                System.out.println("(4) Exit the program.");

                //get user input
                selected = scan.next();
            }
            
            //check if the user put in an invalid answer
            if (!(selected.equals("1") || selected.equals("2") || selected.equals("3") || selected.equals("4")))
            {
                System.out.println("That is not an option. Please try again.");
                isValid = false;
            }
            
            //otherwise it is valid
            else
            {
                isValid = true;
            }

            if (isValid == true)
            {
                //perform option 1 if that is what the user chose
                if (selected.equals("1"))
                {
                    //read that values into a string array split by commas
                    String line = scan.next();
                    String[] data = line.split(",");

                    //check if any of the lengths are invalid
                    if ((Double.parseDouble(data[0]) < 0) || (Double.parseDouble(data[1]) < 0) || (Double.parseDouble(data[2]) < 0)) 
                    {
                        System.out.println("That is not a valid input.");
                        isValid = false;
                        continue;
                    }
                    
                    //check for no-args values
                    else if ((Double.parseDouble(data[0]) == 0) || (Double.parseDouble(data[1]) == 0) || (Double.parseDouble(data[2]) == 0)) 
                    {
                        triangles[c] = new Triangle();
                    }

                    //check if the sides make a valid triangle
                    else if (((Double.parseDouble(data[0]) + Double.parseDouble(data[1]) > (Double.parseDouble(data[2]))) && (Double.parseDouble(data[0]) + Double.parseDouble(data[2]) > (Double.parseDouble(data[1])))))
                    {
                        triangles[c] = new Triangle(Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]));
                        if (data.length == 4) 
                        {
                            triangles[c].setColor(Integer.parseInt(data[3]));
                        }
                        else 
                        {
                            triangles[c].setColor(0);
                        }
                    }
                    
                    //create default triangle
                    else 
                    {
                        triangles[c] = new Triangle();
                    }

                    //increment the counter
                    c += 1;

                    //return to menu
                    isValid = false;
                    continue;
                }
            }
            
            //perform option 2 if that is what the user chose
            if (selected.equals("2"))
            {
                trianglesCopy = new Triangle[c];
                for (int i = 0; i < c; i++)
                {
                    trianglesCopy[i] = triangles[i];
                }
                Arrays.sort(trianglesCopy, new SortByArea());

                //print label row
                System.out.printf("%-10s %-10s %-10s %-10s %-10s %-15s %-20s\n", "Side A", "Side B",  "Side C", "Area", "Color", "Type", "Date Created");

                //display sorted array
                for (int i = 0; i < trianglesCopy.length; i++)
                {
                    //if color wasn't entered, don't print it
                    if (trianglesCopy[i].getColor() == 0)
                    {
                        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-15s %-20s\n", df.format(trianglesCopy[i].tri[0]), df.format(trianglesCopy[i].tri[1]),  df.format(trianglesCopy[i].tri[2]), df.format(trianglesCopy[i].getArea()), " ", trianglesCopy[i].getType(), trianglesCopy[i].d1);

                    }

                    //otherwise print it
                    else System.out.printf("%-10s %-10s %-10s %-10s %-10s %-15s %-20s\n", df.format(trianglesCopy[i].tri[0]), df.format(trianglesCopy[i].tri[1]),  df.format(trianglesCopy[i].tri[2]), df.format(trianglesCopy[i].getArea()), trianglesCopy[i].getColor(), trianglesCopy[i].getType(), trianglesCopy[i].d1);

                }
                isValid = false;
                continue;
            }

            //perform option 3 if that is what the user chose
            if (selected.equals("3"))
            {
                boolean colorExist = false;
                int color;
                do{
                    System.out.println("Please enter a color");
                    color = Integer.parseInt(scan.next());
                    if (color > 0 && color <= 7) colorExist = true;
                    else colorExist = false;
                } while (colorExist == false);
                for (int i = 0; i < c; i++)
                {
                    if (triangles[i].getColor() == color)
                    {
                        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-20s\n", df.format(triangles[i].tri[0]), df.format(triangles[i].tri[1]),  df.format(triangles[i].tri[2]), df.format(triangles[i].getArea()), triangles[i].getType(), triangles[i].d1);
                    }
                    else continue;
                }

                //return to the menu
                isValid = false;
                continue;
            }

            //perform option 4 if that is what the user chose
            if (selected.equals("4"))
            {
                System.exit(0);
            }
        } while (true);
    }
}

class SortByArea implements Comparator<Triangle>
{
    @Override
    public int compare(Triangle t1, Triangle t2)
    {
        if (t1.getArea() > t2.getArea()) return 1;
        else if (t1.getArea() < t2.getArea()) return -1;
        else return 0;
    }
}
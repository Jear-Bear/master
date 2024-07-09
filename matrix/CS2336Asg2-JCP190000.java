/*
Name: Jared Perlmutter
ID: JCP 190000
Date: 9/07/20
*/
import java.util.Scanner;
import java.io.*;

class main
{
    public static void main (String[] args) throws FileNotFoundException
    {
        Scanner scan = new Scanner(System.in);
        String in;
        do
        {
            System.out.println("Please enter the filename: ");
            in = scan.next();
            if (in.equals("*")) //exit the program if the user enters "*"
            {
                System.exit(0);
            }
            Scanner input = new Scanner(new File(in));
            int r = Integer.parseInt(input.next()); //read in # of rows
            int c = Integer.parseInt(input.next()); //read in # of columns
            int[][] arr = new int[r][c];
            matrix.setArr(arr, r, c, input);
            matrix.display(arr, r, c);
            System.out.println(search.look(arr, r, c));
        } while (true);
        
    }
}
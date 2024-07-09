import java.util.Scanner;
import java.io.*;

public class matrix {
    static void display(int[][] arr, int r, int c) //iterates through matrix and displays each element
    {
        for (int i = 0; i < r; i++)
        {
            for (int j = 0; j < c; j++)
            {
                System.out.print(arr[i][j]);
            }
            System.out.print("\n");
        }
    }

    static void setArr(int[][] arr, int r, int c, Scanner input) //reads in values from data and puts into matrix
    {
        for (int i = 0; i < r; i++)
        {
            for (int j = 0; j < c; j++)
            {
                arr[i][j] = Integer.parseInt(input.next());
            }
        }
    }
}

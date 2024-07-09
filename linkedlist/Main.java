/*Written by Jared Perlmutter for CS2336.504, assignment 4, starting October 5, 2020.
NetID: JCP190000
This program utilizes linked lists to keep track of all words in a text document. It produces a cross-reference, or a concordance.
It keeps track each of the words in a given file, the number of times it occurs, and which line numbers contain the word.
It also keeps track of the total number of words, the number of unique words, and the number of lines*/

import java.util.Scanner;
import java.io.*;
public class Main
{
public static void main(String args[]) throws FileNotFoundException
{
    //declare/initialize linkedlist, scanner, input string and file
    LinkedList list = new LinkedList();
    Scanner scan = new Scanner(System.in);
    File file;

    //string to store lines from scanner
    String line;

    //string array to split lines into by space
    String[] lineWords;

    //counter
    int c = 0;

    //boolean utilized in do-while loop for when the end of the file is reached
    boolean done = false;

    //boolean to tell the program if it should ignore a certain character
    boolean ignore;

    do
    {
        //ask for file name
        System.out.println("Please enter the filename: ");
        line = scan.next();
        file = new File(line);

        //check if file exists
        if (!file.exists())
        {
            System.out.println("That file doesn't exist. Please try again.");
            continue;
        }

        //read from file
        scan = new Scanner(file);

        //loop to display lines from file
        while (scan.hasNextLine())
        {
            line = scan.nextLine();
            c += 1;
            System.out.println(c + " " + line);

            //remove any unnecessary characters
            line = Helper.format(line);

            //split line into array of words
            lineWords = line.split(" "); 

            //insert words into linkedlist 
            for (int i = 0; i < lineWords.length; i++)
            {
                //check if word should be ignored
                ignore = Helper.shouldIgnore(lineWords[i]);

                //check if the current string is a word that should be ignored
                if (ignore) continue;

                //check if line contains characters
                else if (line.matches(".*\\w.*")) list.insert(lineWords[i], c);

                //otherwise don't add to the list
                else continue;
            }
        }

        //close scanner and exit do-while loop
        scan.close();
        done = true;

    } while (!done);

    //print blank line
    System.out.println("");

    //display list
    list.display(c);
} 
} 
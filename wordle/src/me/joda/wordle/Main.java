package me.joda.wordle;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;  

public class Main {
	
	static int guessesLeft;
	
	public static void main(String[] args) throws IOException
    {
		JFrame f=new JFrame();//creating instance of JFrame  
		JButton b=new JButton("click");//creating instance of JButton  
		b.setBounds(130,100,100, 40);//x axis, y axis, width, height  
		f.add(b);//adding button in JFrame  
        
		f.setSize(400,500);//400 width and 500 height  
		f.setLayout(null);//using no layout managers  
		f.setVisible(true);//making the frame visible  
		getAnswer ans = new getAnswer();
		String wordToGuess = ans.getAnswer();
		String guess = "";
		boolean correct = false;
		
		userInput input = new userInput();
		
		guessesLeft = 6;
		
		compare comp = new compare();
		Arrays.fill(comp.green, "");
		comp.yellow.clear();
		comp.none.clear();
		
		while (!correct)
		{
			Scanner reader = new Scanner(System.in);
			if (guessesLeft == 0)
			{
				System.out.println("You fail!");
				reader.close();
				break;
			}
			System.out.println("Enter a guess (" + guessesLeft + " left): ");
			guess = input.getUserInput(reader, wordToGuess);
			
			if (guess.equalsIgnoreCase(wordToGuess))
			{
				System.out.println("Congrats! You're right");
				correct = true;
			}
			guessesLeft -= 1;
		}
    }
}
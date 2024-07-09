package me.joda.wordle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class userInput {

	Main main = new Main();
	compare comp = new compare();
	
	public String getUserInput(Scanner reader, String answer) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new FileReader("userAnswers.txt"));

		String inputLine;
		List<String> lineList = new ArrayList<String>();
		while ((inputLine = bufferedReader.readLine()) != null) {
			lineList.add(inputLine);
		}
		
		String userGuess = reader.nextLine();
		
		if (userGuess.length() != 5)
		{
			System.out.println("Please enter a 5 letter word!");
			getUserInput(reader, answer);
		}
		else if( userGuess.length() == 5 && !lineList.contains(userGuess.toLowerCase()))
		{
			System.out.println("Please enter a valid word!");
			getUserInput(reader, answer);
		}
		else
		{
			comp.compare(userGuess, answer);
		}
		return userGuess;

	}
}
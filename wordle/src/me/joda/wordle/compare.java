package me.joda.wordle;

import java.util.ArrayList;

public class compare {

	Main main = new Main();
	final String ANSI_RESET = "\u001B[0m";
	final String ANSI_BLACK = "\u001B[30m";
	final String ANSI_RED = "\u001B[31m";
	final String ANSI_GREEN = "\u001B[32m";
	final String ANSI_BLUE = "\u001B[34m";
	final String ANSI_PURPLE = "\u001B[35m";
	final String ANSI_CYAN = "\u001B[36m";
	final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	String[] green = new String[]{" ", " ", " ", " ", " "};
	ArrayList<String> yellow = new ArrayList<>();
	ArrayList<String> none = new ArrayList<>();
	
	public void compare(String guess, String answer)
	{
		String[] g = guess.split("(?!^)");
		String[] a = answer.split("(?!^)");
		ArrayList<String> userGuess = new ArrayList<>();
		ArrayList<String> gameAnswer = new ArrayList<>();
		
		
		for (String letter : g)
		{
			userGuess.add(letter);
		}
		for (String letter : a)
		{
			gameAnswer.add(letter);
		}
		
		
		for (String letter : userGuess)
		{
			System.out.println(letter + " " + gameAnswer.get(userGuess.indexOf(letter)));
			//if character is in answer, and right spot
			if (gameAnswer.get(userGuess.indexOf(letter)).equalsIgnoreCase(letter))
			{
				green[userGuess.indexOf(letter)] = letter;
			}
			
			//if character is in answer, but not right spot
			else if(gameAnswer.contains(letter))
			{
				int timesGame = 0;
				int timesUser = 0;
				int timesYellow = 0;
				for (String let1 : userGuess)
				{
					if (let1.equalsIgnoreCase(letter))
					{
						timesUser += 1;
					}
				}
				for (String let2 : yellow)
				{
					if (let2.equalsIgnoreCase(letter))
					{
						timesYellow += 1;
					}
				}
				for (String let3 : gameAnswer)
				{
					if (let3.equalsIgnoreCase(letter))
					{
						timesGame += 1;
					}
				}
				if (timesUser >= timesGame)
				{
					int timesAdd = timesGame - timesYellow;
					while (timesAdd > 0)
					{
						yellow.add(letter);
						timesAdd -= 1;
					}
				}
			}
			
			//if character is in not in answer
			else
			{
				if (!none.contains(letter))
				{
					none.add(letter);
				}
			}
		}
		

		System.out.print("GREEN: ");
		for (String letter : green)
		{
			System.out.print(letter + " ");
		}
		System.out.println();
		
		if (!yellow.isEmpty())
		{
			System.out.print("YELLOW: ");
			for (String letter : yellow)
			{
				System.out.print(letter + " ");
			}
			System.out.println();
		}
		
		if (!none.isEmpty())
		{
			System.out.print("NONE: ");
			for (String letter : none)
			{
				System.out.print(letter + " ");
			}
			System.out.println();
		}
	}
}
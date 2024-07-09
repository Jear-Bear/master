package me.joda.wordle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class getAnswer
{
	public String getAnswer() throws IOException {
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader("newAnswers.txt"));
		
		String inputLine;
		List<String> lineList = new ArrayList<String>();
		while ((inputLine = bufferedReader.readLine()) != null) {
			lineList.add(inputLine);
		}
		
		bufferedReader.close();
		
		Random rand = new Random();
		int randomIndex = rand.nextInt(lineList.size());
		String word = lineList.get(randomIndex);
		System.out.println(word);
		
		File file = new File("oldAnswers.txt");
		FileWriter writer = new FileWriter(file, true);
		writer.write(word + "\n");
		writer.close();
		

		//delete selected game word from list of game answers
		//delete(inputFile, randomIndex + 1, 1);
		
		return word;

	}
	
	static void delete(String filename, int startline, int numlines)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(filename));
 
			//String buffer to store contents of the file
			StringBuffer sb=new StringBuffer("");
 
			//Keep track of the line number
			int linenumber=1;
			String line;
 
			while((line=br.readLine())!=null)
			{
				//Store each valid line in the string buffer
				if(linenumber<startline||linenumber>=startline+numlines)
					sb.append(line+"\n");
				linenumber++;
			}
			if(startline+numlines>linenumber)
				System.out.println("End of file reached.");
			br.close();
 
			FileWriter fw=new FileWriter(new File(filename));
			//Write entire string buffer into the file
			fw.write(sb.toString());
			fw.close();
		}
		catch (Exception e)
		{
			System.out.println("Something went horribly wrong: " + e.getMessage());
		}
	}
}
package me.joda.shuffle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main
{
	static int c = 0;
   public static void main(String[] argv) throws IOException
   {
	  File shuffled = new File("C:\\Users\\jperl\\OneDrive\\Desktop\\blocks.txt");
	  File folder = new File("C:\\Users\\jperl\\OneDrive\\Desktop\\to shuffle\\block");
      File[] rawList = folder.listFiles();
	  BufferedReader bufReader = new BufferedReader(new FileReader(shuffled)); 
	  ArrayList<String> shuffledList = new ArrayList<>(); 
	  Scanner scanner = new Scanner(shuffled);
	  while (scanner.hasNextLine()) {
	     String line = scanner.nextLine();
	     line = line.replaceAll("\\s","");
	     shuffledList.add(line);
	  }
	  bufReader.close();
	  Collections.shuffle(shuffledList);
      for (File f : rawList){
    	  renameFile(f, shuffledList.get(c));
        System.out.println(f.getName());
        System.out.println(shuffledList.get(c));
        c +=1;
        if (c == shuffledList.size()) return;
      }
      
   }
   
   public static void renameFile(File toBeRenamed, String new_name)
		    throws IOException {
		    //need to be in the same path
		    File fileWithNewName = new File(toBeRenamed.getParent(), new_name);
		    if (fileWithNewName.exists()) {
		        throw new IOException("file exists");
		    }
		    // Rename file (or directory)
		    boolean success = toBeRenamed.renameTo(fileWithNewName);
		    if (!success) {
		        // File was not successfully renamed
		    }
		}
   
   
}
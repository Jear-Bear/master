/*Written by Jared Perlmutter for CS2336.504, assignment 4, starting October 5, 2020.
NetID: JCP190000
This file contains numerous helper functions for functionality*/

public class Helper 
{
    //remove unnecessary characters
    static public String format(String s)
    {
        s = s.replace(",", "");
        s = s.replace(".", "");
        s = s.replace(";", "");
        s = s.replace(":", "");
        s = s.replace("\"", "");
        s = s.replace("?", "");
        s = s.replace("(", "");
        s = s.replace(")", "");
        s = s.replace("!", "");
        s = s.replace("-", " ");
        s = s.replace("\r\n", "").replace("\n", "");
        s = s.toLowerCase();
        return s;
    }

    //function to ignore a, an, and the
    static public boolean shouldIgnore(String s)
    {
        if (s.equals("the") || s.equals("The") || s.equals("a") || s.equals("A") || s.equals("an") || s.equals("An")) return true;
        else return false;
    }
}

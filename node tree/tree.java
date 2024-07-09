import java.util.Vector;
/*Written by Jared Perlmutter for CS2336.504, assignment 5, starting October 17, 2020.
NetID: JCP190000
This file contains the functions and defining classes for binary trees and nodes.*/

public class tree
{
    //declare binary tree values
    Node root;
    int count = 0;;

    private static class Node
    {
        //node data
        String word; //stores a specific word from the document
        Vector<Integer> lines = new Vector<>(); //stores the line numbers of other duplicates of this word
        Node right; //points to right child
        Node left; //points to left child

        //constructor
        public Node(String w, int l)
        {
            this.word = w;
            this.lines.add(l);
            this.left = null;
            this.right = null;
        }
    }


    //inserts nodes into tree
    public void insert(String s, int w) 
    {
        root = insert(root, s, w);
    }

    //inserts node into tree in order with recursive calls
    private Node insert(Node r, String s, int w) 
    {
        Node n = new Node(s, w);

        //return the node if the tree is empty
        if (r == null) 
        {
            r = n;
        }
  
        //if the node is greater than the data, insert to the left
        if (r.word.compareTo(n.word) > 0) 
        {
            r.left = insert(r.left, s, w);
        }
            
        //if the node is less than the data, insert to the right
        else if (r.word.compareTo(n.word) < 0)
        {
            r.right = insert(r.right, s, w);
        }

        //if the node matches the to be inserted data, add the line number to the vector in the node
        else if (r.word.compareTo(n.word) == 0) 
        {
            r.lines.add(w);
        }

        //return the node
        return r;
    }

    //counts total number of nodes in tree
    int treeCount(Node r)
    {
        if (r == null) return 0;

        else
        {
            //recursive call of counter function, use size of vector to determine number of duplicates if there are any
            return nodeCount(r) - 1 + treeCount(r.left) + treeCount(r.right);
        }
    }

    //counts number of node n (including duplicates)
    int nodeCount(Node n)
    {
        return n.lines.size();
    }

    //counts the number of unique nodes in the tree
    int treeCountUnique(Node r)
    {
        //start counter at 1 to account for root
        int c = 1;
    
        //check if the node is null
        if (r == null) return 0;
        
        //otherwise recursively count the nodes
        else
        {
            c += treeCountUnique(r.left);
            c += treeCountUnique(r.right);
            return c;
        }
    }

    //helper function that traverses through tree and displays information about nodes
    public void displayTree(Node r)
    {
        //keep track of number of nodes
        int size = r.lines.size() - 1;

        //check left child
        if (r.left != null) displayTree(r.left);

        //display the word and how many times it appears
        System.out.print(r.word + " " + size + " ");

        //start counter at 1 to account for loop
        for (int i = 1; i < r.lines.size(); i++)
        {
            //if at last value in vector, don't print a comma
            if (i == size) System.out.print(r.lines.get(i) + " ");

            //otherwise, print a comma
            else System.out.print(r.lines.get(i) + ", ");
        }
    
        //print newline
        System.out.println("");

        //check right child
        if (r.right != null) displayTree(r.right);

    }

    //displays tree
    public void display(int lines) 
    {
        //assign the root to a reference variable
        Node ref = root;

        //store total number of lines
        int ls = lines;

        //store total number of words
        int totalWords = treeCount(ref) + 1;

        //store number of unique words
        int uniqueWords = treeCountUnique(ref);

        //check if tree is empty
        if(ref == null) 
        {
            System.out.println("Tree is empty");  
            return;
        }
 
        //traverse through tree
        displayTree(ref);

        //print newline for aesthetic reasons
        System.out.println("");

        //display number of total words
        System.out.println("Total words: " + totalWords);

        //display number of unique words
        System.out.println("Unique words: " + uniqueWords);

        //display number of lines
        System.out.println("Number of lines: " + ls);
    }
}
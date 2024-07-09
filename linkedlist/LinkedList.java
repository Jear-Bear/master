import java.util.Vector;
/*Written by Jared Perlmutter for CS2336.504, assignment 4, starting October 5, 2020.
NetID: JCP190000
This file contains the functions and defining classes for linkedlists and nodes.*/

public class LinkedList
{
    //declare LinkedList values
    Node head;
    Node tail;

    private static class Node
    {
        //node data
        String word; //stores a specific word from the document
        int line; //stores the line number that the word was located on
        int dupe = 0; //stores the number of times this word appears in the document
        Vector<Integer> lines = new Vector<>(); //stores the line numbers of other duplicates of this word
        Node next;
        Node prev;

        //constructor
        public Node(String w, int l)
        {
            this.word = w;
            this.line = l;
            this.next = null;
        }
    }

    //inserts node at head of list
    public void insert(String s, int l)
    {
        //start at the head of the linkedlist
        Node current = head;
        Node n = new Node(s, l);

        //if the list is empty, insert the node at the head
        if (head == null)
        {
            insertAtHead(n);
        }


        else
        {
            //traverse through the list
            while (current != null)
            {
                //if there's a node after this one
                if (current.next != null)
                {
                    //if the new node is greater than or equal to the current node, insert it after
                    if (current.word.compareTo(n.word) <= 0 && n.word.compareTo(current.next.word) <= 0)
                    {
                        insertAfter(current, n);
                        break;
                    }
                }

                //if the new node is less than or equal to the current node AND the current node is the head, insert it before
                if (current == head && current.word.compareTo(n.word) >= 0)
                {
                    insertBefore(current, n);
                    break;
                }

                //if the new node is greater than or equal to the current node AND the current node is the tail, insert it after
                else if (current == tail && current.word.compareTo(n.word) <= 0)
                {
                    insertAfter(current, n);
                    break;
                }

                //iterate to the next node
                current = current.next;
            }
        }
    }

    //insert a node before a specified node
    public void insertBefore(Node a, Node n)
    {
        //check if the linkedlist is null or if we're inserting before the head
        if (a == null || a == head)
        {
            insertAtHead(n);
            n = head;
        }

        else
        {
            //check that the node we're inserting before isn't the head
            if (a != head)
            {
                //make the new ndoe point back to what the previous node was pointing back to
                n.prev = a.prev;

                //make the node before the next node point to the new node
                a.prev.next = n;
            }

            //make the next node point back towards the new node
            a.prev = n;

            //make the new node point towards the next node
            n.next = a;

            //check if we're inserting before the head
            if (a == head)
            {
                //set the head to the new node
                head = n;
                head.prev = null;
            }
        }
    }

    //insert a node after a specified node
    public void insertAfter(Node a, Node n)
    {
        //check if the previous node is the tail
        if (a == null) 
        { 
            insertAtTail(n);
        }  
    
        else
        {
            //check that we're not inserting after the tail
            if (a != tail)
            {
                //make the new node point to the node after the node we're inserting after
                n.next = a.next;

                //make the node after the node we're inserting after point back to the new node
                a.next.prev = n;
            }

            //make the node we're inserting after point to the new node
            a.next = n;

            //make the new node point back towards the node we're inserting after
            n.prev = a;

            //check if we're trying to insert after the tail
            if (a == tail)
            {
                //set the tail to the new node
                tail = n;
                tail.next = null;
            }
        }
    }

    //insert node at head of list
    public void insertAtHead(Node n)
    {
        //check if list is empty
        if (head == null)
        {
            head = n;
            tail = n;
        }

        else
        {
            //Assign the head to be the node after the new node
            n.next = head; 

            //make the previous value of the new node null
            head.prev = n; 
        
            //make the head point to the new node
            head = n; 
        }
    }

    //insert node at tail of list
    public void insertAtTail(Node n)
    {
        //check if list is empty
        if (head == null)
        {
            head = n;
            tail = n;
        }

        else
        {
            //make tail point to new node
            tail.next = n;

            //make the new node point back to the old tail
            n.prev = tail;

            //make the tail point to the new node
            tail = n;
        }
    }

    //removes duplicate nodes in linkedlist
    void removeDuplicates()
    {
        //set counter and start at head of linkedlist
        Node current = head;
        int count;

        while (current != null) 
        {
            count = 0;
            Node temp = current;

            //iterate as long as there are back-to-back duplicates
            while(temp != null && temp.word.equals(current.word)) 
            {
                current.lines.add(temp.line);
                temp = temp.next;
                count += 1;
            }

            //store number of duplicates in node
            current.dupe = count;
            current.next = temp;

            //traverse to next node
            current = current.next;
        }
    }

    //counts the number of nodes in a linkedlist
    int listCount()
    {
        int count = 0;
        Node n = head;
        while (n != null)
        {
            n = n.next;
            count += 1;
        }
        return count;
    }

    //displays list
    public void display(int lines) 
    {
        //store total number of lines
        int ls = lines;
        //store total number of words
        int totalWords = this.listCount() + 1;

        //create linkedlist without duplicates
        this.removeDuplicates();
        Node ref = this.head;

        //store number of unique words
        int uniqueWords = this.listCount();

        //check if list is empty
        if(head == null) 
        {
            System.out.println("List is empty");  
            return;
        }
 
        //traverse through list
        while(ref != null) 
        {   
            //display the word and how many times it appears
            System.out.print(ref.word + " " + ref.dupe + " ");

            for (int i = 0; i < ref.dupe; i++)
            {
                //if at last value in vector, don't print a comma
                if (i == ref.dupe - 1) System.out.print(ref.lines.get(i) + " ");

                //otherwise, print a comma
                else System.out.print(ref.lines.get(i) + ", ");
            }

            //go to next node, and print a newline
            ref = ref.next;  
            System.out.println("");
        }  

        //print newline for aesthetic reasons
        System.out.println("");

        //display number of total words
        System.out.println("Total words: " + totalWords);

        //display number of unique words
        System.out.println("Unique words: " + uniqueWords);

        //display number of unique words
        System.out.println("Number of lines: " + ls);
    }  
}
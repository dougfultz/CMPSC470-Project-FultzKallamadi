import java.io.*;
import java.lang.String;

class P1 {
    private static String shell = "P1# ";
    
    public static void main(String args[]){
        //The test driver used to test your SymbolTable implementation.
        System.out.println(
            "Project 1 test driver. Enter any of the following commands:\n"+
            "  (Command prefixes are allowed)\n"+
            "\tOpen (a new scope)\n"+
            "\tClose (innermost current scope)\n"+
            "\tQuit (test driver)\n"+
            "\tDump (contents of symbol table)\n"+
            "\tInsert (symbol,integer pair into symbol table)\n"+
            "\tLookup (lookup symbol in top scope)\n"+
            "\tGlobal (global lookup of symbol in symbol table)\n"+
            "");
    
        // Complete this
        
        //https://docs.oracle.com/javase/tutorial/essential/io/cl.html
        Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        
        String input;
        
        //https://docs.oracle.com/javase/tutorial/java/nutsandbolts/while.html
        do{
            input = c.readLine(shell);
            //System.out.println(input);
            
            //https://docs.oracle.com/javase/7/docs/technotes/guides/language/strings-switch.html
            switch(input){
                case "open":
                    //do stuff
                    System.out.println("New scope opened.");
                    break;
                case "close":
                    //do stuff
                    System.out.println("Top scope closed.");
                    break;
                case "quit":
                    System.out.println("Testing done");
                    break;
                case "dump":
                    //do stuff
                    break;
                case "insert":
                    //do stuff
                    
                    //Contents of symbol table:
                    //{blue=(blue:1836)}
                    //{blue=(blue:1845), green=(green:1848)}
                    break;
                case "lookup":
                    //do stuff
                    //found:
                    //(blue:1836) found in top scope
                    //not found:
                    //green not found in top scope
                    break;
                case "global":
                    //same as lookup
                    break;
                default:
                    break;
            }
            
        }while(!input.equals("quit"));
        
    } // main
} // class P1

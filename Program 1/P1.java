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
        
        String input = c.readLine(shell);
        
    } // main
} // class P1

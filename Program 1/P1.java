import java.io.*;
import java.lang.String;
import java.lang.Integer;

class P1 {
    private static String shell = "P1# ";
    private static String enterName = "Enter symbol: ";
    private static String enterValue = "Enter associated integer: ";
    
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
        String words[];
        SymbolTable sTable = new SymbolTable();
        String strName;
        String strValue;
        int intValue;
        TestSym symbol;
        
        //https://docs.oracle.com/javase/tutorial/java/nutsandbolts/while.html
        do{
            input = c.readLine(shell);
            //System.out.println(input);
            
            //https://stackoverflow.com/questions/11607496/trim-a-string-in-java-to-get-first-word
            words = input.split(" ");
            
            //https://docs.oracle.com/javase/7/docs/technotes/guides/language/strings-switch.html
            switch(words[0]){
                case "open":
                    sTable.openScope();
                    System.out.println("New scope opened.");
                    break;
                case "close":
                    //https://docs.oracle.com/javase/tutorial/essential/exceptions/try.html
                    try{
                        sTable.closeScope();
                        System.out.println("Top scope closed.");
                    //https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html
                    }catch(EmptySTException e){
                        System.out.println("ERROR: SymbolTable is empty.");
                    }
                    break;
                case "quit":
                    System.out.println("Testing done");
                    System.exit(0);
                    break;
                case "dump":
                    sTable.dump(System.out);
                    break;
                case "insert":
                    //Gather input
                    //http://alvinalexander.com/java/java-array-length-example-length-method
                    //https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
                    switch(words.length){
                        case 1:
                            strName = c.readLine(enterName);
                            strValue = c.readLine(enterValue);
                            break;
                        case 2:
                            strName = words[1];
                            strValue = c.readLine(enterValue);
                            break;
                        case 3:
                            strName = words[1];
                            strValue = words[2];
                            break;
                        default:
                            System.out.println("Expecting name, value or both.");
                            continue;
                    }
                    
                    //Parse value
                    intValue = Integer.parseInt(strValue);
                    
                    //Create symbol
                    symbol = new TestSym(strName,intValue);
                    
                    //Insert symbol into table
                    sTable.insert(symbol);
                    break;
                case "lookup":
                    //Gather input
                    switch(words.length){
                        case 1:
                            strName = c.readLine(enterName);
                            break;
                        case 2:
                            strName = words[1];
                            break;
                        default:
                            System.out.println("Expecting name.");
                            continue;
                    }
                    
                    //Store symbol
                    symbol = (TestSym)sTable.localLookup(strName);
                    
                    //Output symbol
                    try{
                        System.out.println(symbol.toString()+" found in symbol table.");
                    }catch(NullPointerException e){
                        System.out.println(strName+" not found in top scope.");
                    }
                    break;
                case "global":
                    //Gather input
                    switch(words.length){
                        case 1:
                            strName = c.readLine(enterName);
                            break;
                        case 2:
                            strName = words[1];
                            break;
                        default:
                            System.out.println("Expecting name.");
                            continue;
                    }
                    
                    //Store symbol
                    symbol = (TestSym)sTable.globalLookup(strName);
                    
                    //Output symbol
                    try{
                        System.out.println(symbol.toString()+" found in symbol table.");
                    }catch(NullPointerException e){
                        System.out.println(strName+" not found in global scope.");
                    }
                    break;
                default:
                    System.out.println("Command not found.");
                    break;
            }
            
        }while(!input.equals("quit"));
        
    } // main
} // class P1

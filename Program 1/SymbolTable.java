import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

//https://docs.oracle.com/javase/tutorial/essential/exceptions/declaring.html
class SymbolTable {
    private LinkedList sTable;

    SymbolTable(){
        //The class constructor; initialize SymbolTable to a symbol table with no scopes.
        
        //Create an empty Linked List
        //https://docs.oracle.com/javase/tutorial/collections/interfaces/list.html
        //https://docs.oracle.com/javase/8/docs/api/index.html?java/util/List.html
        sTable = new LinkedList();
    }

    public void openScope(){
        //Add a new, initially empty scope to the list of scopes contained in this SymbolTable.
        
        //Add an empty HashMap to the list
        //https://docs.oracle.com/javase/8/docs/api/index.html?java/util/HashMap.html
        sTable.addFirst(new HashMap());
    }

    public void closeScope() throws EmptySTException{
        //If the list of scopes in this SymbolTable is empty, throw an EmptySTException. Otherwise, remove the current (front) scope from the list of scopes contained in this SymbolTable.
        
        //Remove last element in list
        try{
            sTable.removeFirst();
        }catch(NoSuchElementException e){
            //https://docs.oracle.com/javase/tutorial/essential/exceptions/throwing.html
            throw new EmptySTException();
        }
    }

    public void insert(Symb s) throws EmptySTException,DuplicateException {
        //If the list of scopes in this SymbolTable is empty, throw an EmptySTException. If the current (first) scope contains a Symb whose name is the same as that of s (ignoring case), throw a DuplicateException. Otherwise, insert s into the current (front) scope of this SymbolTable.
        
        //Get first element in list
        HashMap current;
        try{
            current = (HashMap)sTable.getFirst();
        }catch(NoSuchElementException e){
            throw new EmptySTException();
        }
        
        //Check for existing key
        //https://stackoverflow.com/a/22336599
        //TODO - make case insensitive
        if(current.containsKey(s.name())){
            throw new DuplicateException();
        }
        
        //Insert key into HashMap
        current.put(s.name(), ((TestSym)s).value());
    }

    public Symb localLookup(String s){
        //If the list of scopes in this SymbolTable is empty, return null. If the current (first) scope contains a Symb whose name is n (ignoring case), return that Symb. Otherwise, return null.
        
        //Get first element in list
        HashMap current;
        try{
            current = (HashMap)sTable.getFirst();
        }catch(NoSuchElementException e){
            return null;
        }
        
        //Check for existing key
        //TODO - make case insensitive
        if(current.containsKey(s)){
            return(new TestSym(s,Integer.parseInt(current.get(s).toString())));
        }
        
        return null; // change this
    }

    public Symb globalLookup(String s){
        //If any scope contains a Symb whose name is n (ignoring case), return the first matching Symb found (in the scope nearest to the front of the scope list). Otherwise, return null.
        
        //Iterate over all elements in list
        //http://crunchify.com/how-to-iterate-through-linkedlist-instance-in-java/
        ListIterator listIterator  = sTable.listIterator();
        HashMap current;
        while(listIterator.hasNext()){
            current = (HashMap)listIterator.next();
            
            //Check for existing key
            //TODO - make case insensitive
            if(current.containsKey(s)){
                return(new TestSym(s,Integer.parseInt(current.get(s).toString())));
            }
        }
        
        return null; // change this
    }

    public String toString(){
        //Return a string representation of this SymbolTable
        /* complete this */
        
        //Iterate over all elements in list
        ListIterator listIterator  = sTable.listIterator();
        HashMap current;
        String s="";
        while(listIterator.hasNext()){
            current = (HashMap)listIterator.next();
            
            //Convert each HashMap to a string
            s+=current.toString();
            if(listIterator.hasNext()){
                s+="\n";
            }
        }
        
        return(s);
    }

    void dump(PrintStream ps){
        //This method is for debugging. The contents of this SymbolTable are written to Printstream p (System.out is a Printstream).
        ps.println("Contents of symbol table:");
        ps.println(this.toString());
    }
} // class SymbolTable

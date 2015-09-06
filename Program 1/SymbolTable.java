import java.io.*;
class SymbolTable {

    SymbolTable(){
        //The class constructor; initialize SymbolTable to a symbol table with no scopes.
        /* complete this */
    }

    public void openScope(){
        //Add a new, initially empty scope to the list of scopes contained in this SymbolTable.
        /* complete this */
    }

    public void closeScope() throws EmptySTException{
        //If the list of scopes in this SymbolTable is empty, throw an EmptySTException. Otherwise, remove the current (front) scope from the list of scopes contained in this SymbolTable.
        /* complete this */
    }

    public void insert(Symb s){
        //If the list of scopes in this SymbolTable is empty, throw an EmptySTException. If the current (first) scope contains a Symb whose name is the same as that of s (ignoring case), throw a DuplicateException. Otherwise, insert s into the current (front) scope of this SymbolTable.
        /* complete this */
    }

    public Symb localLookup(String s){
        //If the list of scopes in this SymbolTable is empty, return null. If the current (first) scope contains a Symb whose name is n (ignoring case), return that Symb. Otherwise, return null.
        /* complete this */
        return null; // change this
    }

    public Symb globalLookup(String s){
        //If any scope contains a Symb whose name is n (ignoring case), return the first matching Symb found (in the scope nearest to the front of the scope list). Otherwise, return null.
        /* complete this */
        return null; // change this
    }

    public String toString(){
        //Return a string representation of this SymbolTable
        /* complete this */
        return ""; // change this
    }

    void dump(PrintStream ps){
        //This method is for debugging. The contents of this SymbolTable are written to Printstream p (System.out is a Printstream).
        /* complete this */
    }
} // class SymbolTable

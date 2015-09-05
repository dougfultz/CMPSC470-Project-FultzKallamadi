import java.io.*;
class SymbolTable {

   SymbolTable() {/* complete this */ }

   public void openScope() {
      /* complete this */ }

   public void closeScope() throws EmptySTException {
      /* complete this */ }

   public void insert(Symb s) {
      /* complete this */ }

   public Symb localLookup(String s) {
      /* complete this */
      return null; // change this
   }

   public Symb globalLookup(String s) {
      /* complete this */
      return null; // change this
   }

   public String toString() {
      /* complete this */
      return ""; // change this
   }

   void dump(PrintStream ps) {
      /* complete this */
   }
} // class SymbolTable

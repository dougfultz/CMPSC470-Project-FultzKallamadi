/**************************************************
*  class used to hold information associated w/
*  Symbs (which are stored in SymbolTables)
*
****************************************************/

class SymbolInfo extends Symb {
 public Kinds kind; // Should always be Var in CSX-lite
 public Types type; // Should always be Integer or Boolean in CSX-lite

 public SymbolInfo(String id, Kinds k, Types t){
	super(id);
	kind = k; 
	type = t;
	}
 public SymbolInfo(String id, int k, int t){
	super(id);
	kind = new Kinds(k); 
	type = new Types(t);
	}
 public String toString(){
             return "("+name()+": kind=" + kind+ ", type="+  type+")";
             }
}


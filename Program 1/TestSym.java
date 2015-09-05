class TestSym extends Symb {
   private int value;
   TestSym(String n,int i) 
   {
	   super(n); 
	   value = i;
   }
   public int value() 
   {
	   return value;
   }
   public String toString() 
   {
	   return "("+name()+":"+String.valueOf(value)+")";
   }
} // class TestSym

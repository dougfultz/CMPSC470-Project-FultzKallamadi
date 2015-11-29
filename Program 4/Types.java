class Types{
 public static final int Character = 0;
 public static final int Integer = 1;
 public static final int Boolean = 2;
 public static final int Void = 3;
 public static final int Error = 4;
 public static final int Unknown = 5;
 public static final int Real = 6;
 public static final int String = 7;

 Types(int i){val = i;}
 Types(){val = Unknown;}

 public String toString() {
	switch(val){
	  case Character: return "Character";
	  case Integer: return "Integer";
	  case Boolean: return "Boolean";
	  case Void: return "Void";
	  case Error: return "Error";
	  case Unknown: return "Unknown";
	  case Real: return "Real";
 	  case String: return "String";
	  default: throw new RuntimeException();
	}
 }

 int val;
}


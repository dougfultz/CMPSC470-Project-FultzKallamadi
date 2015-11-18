class Kinds{
 public static final int Var = 0;
 public static final int Value = 1;
 public static final int Other = 2;

 Kinds(int i){val = i;}
 Kinds(){val = Other;}

 public String toString() {
        switch(val){
          case 0: return "Var";
          case 1: return "Value";
          case 2: return "Other";
          default: throw new RuntimeException();
        }
 }

 int val;
}

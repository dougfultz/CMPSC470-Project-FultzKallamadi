class Kinds{
 public static final int Var = 0;
 public static final int Value = 1;
 public static final int Array = 2;
 public static final int ScalarParm = 3;
 public static final int ArrayParm = 4;
 public static final int Method = 5;
 public static final int Label = 6;
 public static final int Other = 7;

 Kinds(int i){val = i;}
 Kinds(){val = Other;}

 public String toString() {
        switch(val){
          case Var: return "Var";
          case Value: return "Value";
          case Array: return "Array";
          case ScalarParm: return "ScalarParm";
          case ArrayParm: return "ArrayParm";
          case Method: return "Method";
          case Label: return "Label";
          case Other: return "Other";
          default: throw new RuntimeException();
        }
 }

 int val;
}

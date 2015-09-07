//Doug Fultz
//Sujay Kallamadi
//CMPSC470 - Compiler Construction

class TestSym extends Symb {
    private int value;
    TestSym(String n,int i){
        //The class constructor; initialize TestSym to have name s and value i.
        super(n);
        value = i;
    }
    public int value(){
        //Return the value of this TestSym.
        return value;
    }
    public String toString(){
        //Return a string representation of this TestSym object.
        return "("+name()+":"+String.valueOf(value)+")";
    }
} // class TestSym

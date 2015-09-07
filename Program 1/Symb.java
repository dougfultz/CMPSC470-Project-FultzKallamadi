//Doug Fultz
//Sujay Kallamadi
//CMPSC470 - Compiler Construction

class Symb {
    private String name;
    Symb(String n){
        //The class constructor; initialize Symb to have name s.
        name = n;
    }
    public String name(){
        //Return the name of this Symb.
        return name;
    }
    public String toString(){
        //Return a string representation of this Symb object.
        return name;
    }
} // class Symb

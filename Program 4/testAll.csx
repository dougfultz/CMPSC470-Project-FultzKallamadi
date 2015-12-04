class test {            //prog - Rule 1

    //memberdecls - Rule 1
    //fielddecls - Rule 1
    int a;          //fielddecl - Rule 1
    int b = 1;      //fielddecl - Rule 2
    int c[2];       //fielddecl - Rule 3
    const d = 0;    //fielddecl - Rule 4

    //memberdecls - Rule 2
    void fa() {         //methoddecl - Rule 1
        int a;
        return;
    }
    void fb(int a) {    //methoddecl - Rule 2, argdecls - Rule 2
        int b;
        return;
    }
    int fc() {          //methoddecl - Rule 3
        int a;
        return;
    }
    int fd(int a, int b) {  //methoddecl - Rule 4
        int c;
        return 0;
    }
}

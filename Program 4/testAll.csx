class test {            //prog - Rule 1

    //memberdecls - Rule 1
    //fielddecls - Rule 1
    int a;          //fielddecl - Rule 1
    int b = 1;      //fielddecl - Rule 2
    int c[2];       //fielddecl - Rule 3
    const d = 0;    //fielddecl - Rule 4

    //memberdecls - Rule 2
    //methoddecls - Rule 1
    void fa() {         //methoddecl - Rule 1
        int a;          //stmts - Rule 1
        return;         //stmts - Rule 2
    };                  //optionalsemi - Rule 1
    void fb(int a) {    //methoddecl - Rule 2, argdecls - Rule 2, argdecl - Rule 1
        int b;
        return;
    }                   //optionalsemi - Rule 2
    int fc() {          //methoddecl - Rule 3
        int a;
        return;
    }
    int fd(int a, int b[]) {  //methoddecl - Rule 4, argdecls - Rule 1, argdecl - Rule 2
        int c;
        return 0;
    }
    int fe() {
        int a;
        if ( true )   //stmt - Rule 1
            a = 0;      //stmt - Rule 5
        endif
        return 0;
    }
}

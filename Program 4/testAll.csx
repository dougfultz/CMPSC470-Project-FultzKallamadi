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
        return;         //stmt - Rule 10
    }                   //optionalsemi - Rule 2
    int fc() {          //methoddecl - Rule 3
        int a;
        return;
    }
    int fd(int a, int b[]) {  //methoddecl - Rule 4, argdecls - Rule 1, argdecl - Rule 2
        int c;
        return 0;       //stmt - Rule 11
    }
    int fe() {
        int a;
        int i;
        
        //stmt - Rule 1
        if ( true )
            a = 0;      //stmt - Rule 5
        endif
        
        //stmt - Rule 2
        if ( false )
            a = 1;
        else
            a = 2;
        endif
        
        //stmt - Rule 3
        while ( false )
            a = 3;
        
        //stmt - Rule for
        for ( i = 0 ; i < 5 ; i++ )     //forinit - Rule 1, forcondition - Rule 3, forupdate - Rule 3
            {                       //stmt - Rule 14
                a = 4;
            }
        
        //stmt - Rule 4
        here: while ( i < 10 )
            if (true)
                continue here;      //stmt - Rule 13
            else
                break here;         //stmt - Rule 12
            endif
        
        //stmt - Rule 6
        read ( a );
        
        //stmt - Rule 7
        print ( a );
        
        //stmt - Rule 8
        fa();
        
        //stmt - Rule 9
        fb(2);
        
        //stmt - Rule ++id
        ++a;
        
        //stmt - Rule id++
        a++;
        
        //stmt - Rule --id
        --a;
        
        //stmt -Rule id--
        a--;
        
        return 0;
    }
}

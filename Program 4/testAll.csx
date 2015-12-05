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
        int array[5];
        
        int tint=1;                 //type - Rule 1, unit - Rule 4
        char tchar='a';             //type - Rule 2, unit - Rule 5
        bool tbool;                 //type - Rule 3
        
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
        read ( a );                 //read - Rule 2
        read ( a, b );              //read - Rule 1
        
        //stmt - Rule 7
        print ( a );                //print - Rule 2
        print ( a, b );             //print - Rule 1
        
        //stmt - Rule 8
        //unit - Rule 2
        fa();
        
        //stmt - Rule 9
        //unit - Rule 3
        fb(2);                      //args - Rule 2
        fd(2,array);                //args - Rule 1
        
        //stmt - Rule ++id
        ++a;
        
        //stmt - Rule id++
        a++;
        
        //stmt - Rule --id
        --a;
        
        //stmt -Rule id--
        a--;
        
        //expr - Rule 1
        //unit - Rule 7
        //unit - Rule 8
        //unit - Rule 9
        if(true || (false||false))
            a++;
        endif
        
        //expr - Rule 2
        if(false && false)
            a--;
        endif
        
        //expr - Rule 3
        //term - Rule 1
        if(1<2)
            a++;
        endif
        
        //term - Rule 2
        if(1>2)
            a++;
        endif
        
        //term - Rule 3
        if(1<=2)
            a++;
        endif
        
        //term - Rule 4
        if(1>=2)
            a++;
        endif
        
        //term - Rule 5
        if(1==2)
            a++;
        endif
        
        //term - Rule 6
        if(1!=2)
            a++;
        endif
        
        //term - Rule 7
        //factor - Rule 1
        a=1+1;
        
        //factor - Rule 2
        a=1-1;
        
        //factor - Rule 3
        //pri - Rule 1
        a=1*1;
        
        //pri - Rule 2
        a=1/1;
        
        //pri - Rule 3
        //unary - Rule 1
        if(!tbool)
            a++;
        endif
        
        //unary - Rule 2
        print((char)tint);
        
        //unary - Rule 3
        //unit - Rule 1
        //name - Rule 1
        //ident - Rule 1
        a++;
        
        //name - Rule 2
        array[0]=4;
        
        return 0;
    }
}

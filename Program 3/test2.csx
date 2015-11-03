class A {
void main() {
     i=1;


  // TEST1: each IF has a corresponding ELSE

     if (x)  
         if (y)  i=2; 
         else   { i=3; } 
		 endif
     
     else 
         if (y) i=2;
         else i=3;
		 endif
     endif


  // TEST2: each IF has a corresponding ELSE

     if (x) 
         if (y) i=2;
         else   i=3;
		 endif
     else
         if (y) i=2;
         else i=3;
		 endif
	endif

  // TEST3: first nested IF does not have an ELSE

     if (x) {
         if (y) i=4; endif
     }
     else
         i=5;
	endif

  // TEST4: similar syntax as above, but this time the lack of
  // braces make the ELSE go with the inner

     if (x)
         if (y) i=4;
     else
         i=5;
		 endif
	endif


  // TEST5: same structure as TEST2, but different indenting to
  // confuse the human reader.

     if (x)
          if (y)
              i=6;endif
     {
          i=7;}
     if (!z)
		print(true);
	else if (z)
           i=8;
    else
           i=9;
	endif
	endif
  // TEST6: IF statements within WHILE loops

    while (a)
        if (x)
            while (b)
                if (y)
			while (i<100)
                    i++;
			endif
        else
           i=8;endif endif
} }

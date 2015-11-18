/*  Replace this with your solution from project 3 */

import java_cup.runtime.*;

class CSXToken 
{
	int linenum;
	int colnum;
	
	CSXToken(int line,int col) 
	{
		linenum = line;
		colnum = col;
	}
	
	CSXToken(Position p) 
	{
		linenum = p.linenum;
		colnum = p.colnum;
		
	}

}

class CSXIntLitToken extends CSXToken {
	int intValue;
	CSXIntLitToken(int val,Position p) 
	{
	   super(p);
	   intValue = val; 
	}
}

class CSXIdentifierToken extends CSXToken {
	String identifierText;
	CSXIdentifierToken(String text, Position p) 
	{
		super(p);
		identifierText = text;
	}
}

class CSXCharLitToken extends CSXToken {
	char charValue;
	CSXCharLitToken(char val, Position p) {
		super(p);
		charValue=val;
	}
}

class CSXStringLitToken extends CSXToken {
	String stringText; // Full text of string literal,
                          //  including quotes & escapes
	CSXStringLitToken(String text, Position p) {
		super(p);
		stringText=text;
	}
}

// This class is used to track line and column numbers
// Feel free to change to extend it
class Position {
		int  linenum; 			/* maintain this as line number current
                                 		   token was scanned on */
	int  colnum; 			/* maintain this as column number current
                                 		   token began at */
	int  line; 				/* maintain this as line number after
										   scanning current token  */
	int  col; 				/* maintain this as column number after
										   scanning current token  */
	Position()
	{
  		linenum = 1; 	
  		colnum = 1; 	
  		line = 1;  
  		col = 1;
	}									   
	void setpos() 
	{ // set starting position for current token
		linenum = line;
		colnum = col;
	}
}

%%
Digit=[0-9]
Letter=[A-Za-z]
WhiteSpace=[\040\t]


// Tell JLex to have yylex() return a Symbol, as JavaCUP will require
%type Symbol

// Tell JLex what to return when eof of file is hit
%eofval{
return new Symbol(sym.EOF, new  CSXToken(0,0));
%eofval}
%{
Position Pos = new Position();
%}
%%
"+"	{Pos.setpos(); Pos.col +=1;
		return new Symbol(sym.PLUS,	new CSXToken(Pos));}
		
"-"	{Pos.setpos(); Pos.col +=1;
		return new Symbol(sym.MINUS, new CSXToken(Pos));}
		
"="	{Pos.setpos(); Pos.col +=1;
		return new Symbol(sym.ASG,	new CSXToken(Pos));}
		
";"	{Pos.setpos(); Pos.col +=1;
		return new Symbol(sym.SEMI,	new CSXToken(Pos));}
		
"("	{Pos.setpos(); Pos.col +=1;
		return new Symbol(sym.LPAREN, new CSXToken(Pos));}
		
")"	{Pos.setpos(); Pos.col +=1;
		return new Symbol(sym.RPAREN, new CSXToken(Pos));}
		
"{"	{Pos.setpos(); Pos.col +=1;
		return new Symbol(sym.LBRACE, new CSXToken(Pos));}
		
"}"	{Pos.setpos(); Pos.col +=1;
		return new Symbol(sym.RBRACE, new CSXToken(Pos));}
		
[Ii][Ff] {Pos.setpos(); Pos.col +=2;
		return new Symbol(sym.rw_IF, new CSXToken(Pos));}
[Ii][Nn][Tt] {Pos.setpos(); Pos.col +=3;
		return new Symbol(sym.rw_INT,
			new CSXToken(Pos));}
[Bb][Oo][Oo][Ll] {Pos.setpos(); Pos.col +=4;
		return new Symbol(sym.rw_BOOL,
			new CSXToken(Pos));}
[Pp][Rr][Ii][Nn][Tt] {Pos.setpos(); Pos.col +=5;
		return new Symbol(sym.rw_PRINT,
			new CSXToken(Pos));}
{Letter}({Letter}|{Digit})*
		{ Pos.setpos(); Pos.col += yytext().length();
		  return new Symbol(sym.IDENTIFIER,
				new CSXIdentifierToken(
					yytext(), Pos));}
{Digit}+	{ Pos.setpos(); Pos.col += yytext().length();
		  return new Symbol(sym.INTLIT,
		  		new CSXIntLitToken(
					Integer.parseInt(yytext()),
					Pos));}
{WhiteSpace}	{Pos.col +=1;}

\n		{Pos.line +=1; Pos.col = 1;}

.		{System.err.println("Lexical error (line " + Pos.linenum +
				", column " + Pos.colnum +
				"): " + yytext() + " ignored.");
			Pos.col +=1;}

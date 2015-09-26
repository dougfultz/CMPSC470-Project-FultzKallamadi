//Doug Fultz
//Sujay Kallamadi
//CMPSC470 - Compiler Construction

/*  Expand this into your solution for project 2 */

class CSXToken 
{
	int linenum;
	int colnum;
	
	CSXToken() 
	{

	}
	
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
	CSXIntLitToken(int val, Position p) 
	{
	   super(p);
	   intValue=val; 
	}
}

class CSXFloatLitToken extends CSXToken {
	// Expand - should contain floatValue
}

class CSXIdentifierToken extends CSXToken {
	// Expand - should contain identifierText
	
}

class CSXCharLitToken extends CSXToken {
	// Expand - should contain charValue
}

class CSXStringLitToken extends CSXToken {
	// Expand - should contain stringText
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
} ;


//This class is used by the scanner to return token information that is useful for the parser
//This class will be replaced in our parser project by the java_cup.runtime.Symbol class
class Symbol { 
	public int sym;
	public CSXToken value;
	public Symbol(int tokenType, CSXToken theToken) {
		sym = tokenType;
		value = theToken;
	}
}

%%

DIGIT=[0-9]
STRLIT = \"([^\" \\ ]|\\n|\\t|\\\"|\\\\)*\"		// to be fixed

%type Symbol
%eofval{
  return new Symbol(sym.EOF, new CSXToken(0,0));
%eofval}
%{
Position Pos = new Position();
%}

%%
/***********************************************************************
 Tokens for the CSX language are defined here using regular expressions
************************************************************************/
"+"	{
	Pos.setpos();
	Pos.col += 1;
	return new Symbol(sym.PLUS,
		new CSXToken(Pos));
}
"!="	{
	Pos.setpos();
	Pos.col +=2;
	return new Symbol(sym.NOTEQ,
		new CSXToken(Pos));
}
";"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.SEMI,
		new CSXToken(Pos));
}
{DIGIT}+	{
	// This def doesn't check for overflow -- be sure to update it
	Pos.setpos(); 
	Pos.col += yytext().length();
	return new Symbol(sym.INTLIT,
		new CSXIntLitToken(Integer.parseInt(yytext()),
			Pos));
}
//EOL to be fixed so that it accepts different formats

\n	{
	Pos.line += 1;
	Pos.col = 1;
}
" "	{
	Pos.col += 1;
}

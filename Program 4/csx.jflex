/*  Solution for project 2 */

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

class CSXIntLitToken extends CSXToken
{
	int intValue;
	CSXIntLitToken(int val,Position p)
	{
	   super(p);
	   intValue = val;
	}
}

class CSXIdentifierToken extends CSXToken
{
	String identifierText;
	CSXIdentifierToken(String text, Position p)
	{
		super(p);
		identifierText = text;
	}
}

class CSXCharLitToken extends CSXToken
{
	char charValue;
	String charText;
	CSXCharLitToken(char val, String s, Position p)
	{
		super(p);
		charText = s;
		charValue = val;
	}
}

class CSXStringLitToken extends CSXToken
{
	String stringText; // Full text of string literal, including quotes & escapes
	CSXStringLitToken(String text, Position p)
	{
		super(p);
		stringText = text;
	}
}

//Error token class
class CSXErrorToken extends CSXToken
{
	String errorText;

	CSXErrorToken(String text, Position p)
	{
		super(p);
		errorText = text;
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

	int countEOLs(String s)
	{
		final char[] c = s.toCharArray();
		int cnt=0;
		for(int i=0; i < c.length; i++) {
			cnt+=(c[i]=='\n'?1:0);
		}
		return cnt;
	}
	int distFromLastEOL(String s)
	{
		final char[] c = s.toCharArray();
		int dist=1;
		for(int i= c.length-1; i >= 0; i--) {
			if (c[i] == '\n') {
				return dist;
			} else {
				dist++;
			}
		}
		return 0;
	}
}

/*
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
*/

%%

DIGIT=[0-9]
IDENTIFIER=([a-zA-Z][_a-zA-Z0-9]*)
INTLIT=({DIGIT}{DIGIT}*)
NEGINTLIT=(("~")({INTLIT}))
STRCHAR=(([\040!#-\[\]-~])|(\\[\"nt\\]))
STRLIT=((\")({STRCHAR}*)(\"))
LITCHAR=(([\040-&(-\[\]-~])|(\\['nt\\]))
CHARLIT=((\')({LITCHAR})(\'))
COMMENT1=("//"(.)*\n)
NOTCHAR = '[^\'\n\r]*'
NOTSTRING = [\"][^\"\n\r]*[\"]
NOTIDNT = [_0-9][_a-zA-Z0-9]*
OP = "++" | "--"

%type Symbol
%eofval{
  return new Symbol(sym.EOF, new CSXToken(0,0));
%eofval}
%{
Position Pos = new Position();
%}
%state Idnt
%%
/***********************************************************************
 Tokens for the CSX language are defined here using regular expressions
************************************************************************/

[bB][oO][oO][lL] {
	Pos.setpos();
	Pos.col +=4;
	return new Symbol(sym.rw_BOOL,
		new CSXToken(Pos));
}
[bB][rR][Ee][Aa][kK]  {
	Pos.setpos();
	Pos.col +=5;
	return new Symbol(sym.rw_BREAK,
		new CSXToken(Pos));
}
[cC][hH][aA][rR] {
	Pos.setpos();
	Pos.col +=4;
	return new Symbol(sym.rw_CHAR,
		new CSXToken(Pos));
}
[cC][lL][aA][sS][sS]  {
	Pos.setpos();
	Pos.col +=5;
	return new Symbol(sym.rw_CLASS,
		new CSXToken(Pos));
}
[cC][oO][nN][sS][tT]  {
	Pos.setpos();
	Pos.col +=5;
	return new Symbol(sym.rw_CONST,
		new CSXToken(Pos));
}
[cC][oO][nN][tT][iI][nN][uU][eE]  {
	Pos.setpos();
	Pos.col +=8;
	return new Symbol(sym.rw_CONTINUE,
		new CSXToken(Pos));
}
[eE][lL][sS][eE] {
	Pos.setpos();
	Pos.col +=4;
	return new Symbol(sym.rw_ELSE,
	new CSXToken(Pos));
}
[fF][aA][lL][sS][eE]  {
	Pos.setpos();
	Pos.col +=5;
	return new Symbol(sym.rw_FALSE,
		new CSXToken(Pos));
}
[iI][fF]  {
	Pos.setpos();
	Pos.col +=2;
	return new Symbol(sym.rw_IF,
		new CSXToken(Pos));
}
[eE][nN][dD][iI][fF]  {
	Pos.setpos();
	Pos.col +=5;
	return new Symbol(sym.rw_ENDIF,
		new CSXToken(Pos));
}
[iI][nN][tT]  {
	Pos.setpos();
	Pos.col +=3;
	return new Symbol(sym.rw_INT,
		new CSXToken(Pos));
}
[rR][eE][aA][dD] {
	Pos.setpos();
	Pos.col +=4;
	return new Symbol(sym.rw_READ,
		new CSXToken(Pos));
}
[rR][eE][tT][uU][rR][nN] {
	Pos.setpos();
	Pos.col +=6;
	return new Symbol(sym.rw_RETURN,
		new CSXToken(Pos));
}
[pP][rR][iI][nN][tT] {
	Pos.setpos();
	Pos.col +=5;
	return new Symbol(sym.rw_PRINT,
		new CSXToken(Pos));
}
[tT][rR][uU][eE] {
	Pos.setpos();
	Pos.col +=4;
	return new Symbol(sym.rw_TRUE,
		new CSXToken(Pos));
}
[vV][oO][iI][dD] {
	Pos.setpos();
	Pos.col +=4;
	return new Symbol(sym.rw_VOID,
		new CSXToken(Pos));
}
[wW][hH][iI][lL][eE]  {
	Pos.setpos();
	Pos.col +=5;
	return new Symbol(sym.rw_WHILE,
			new CSXToken(Pos));
	}
[fF][oO][rR] {
	Pos.setpos();
	Pos.col +=3;
	return new Symbol(sym.rw_FOR, new CSXToken(Pos));
}

{IDENTIFIER}/{OP}    {
	yybegin(Idnt);
	Pos.setpos();
	Pos.col += yytext().length();
	return new Symbol(sym.IDENTIFIER,
		new CSXIdentifierToken(yytext().toLowerCase(), Pos));
}
{IDENTIFIER}    {
	Pos.setpos();
	Pos.col += yytext().length();
	return new Symbol(sym.IDENTIFIER,
		new CSXIdentifierToken(yytext().toLowerCase(), Pos));
}
"("	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.LPAREN,
		new CSXToken(Pos));
}
")"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.RPAREN,
		new CSXToken(Pos));
}
"["	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.LBRACKET,
		new CSXToken(Pos));
}
"]"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.RBRACKET,
		new CSXToken(Pos));
}
"="	{
	Pos.setpos();
	Pos.col +=2;
	return new Symbol(sym.ASG,
		new CSXToken(Pos));
}
";"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.SEMI,
		new CSXToken(Pos));
}

{OP}/{IDENTIFIER} {
	Pos.setpos();
	Pos.col +=2;
	int operator;
	if (yytext().charAt(0)== '+')
    	  operator = sym.INC;
      else
      	  operator = sym.DEC;
	return new Symbol(operator,
		new CSXToken(Pos));
}

<Idnt>{OP} {
	Pos.setpos();
	Pos.col +=2;
	yybegin(YYINITIAL);
	int operator;
	if (yytext().charAt(0)== '+')
    	  operator = sym.INC;
      else
      	  operator = sym.DEC;
	return new Symbol(operator,
		new CSXToken(Pos));
}
"+"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.PLUS,
		new CSXToken(Pos));
}
"-"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.MINUS,
		new CSXToken(Pos));
}
"*"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.TIMES,
		new CSXToken(Pos));
}
"/"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.SLASH,
		new CSXToken(Pos));
}
"=="	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.EQ,
		new CSXToken(Pos));
}
"!="	{
	Pos.setpos();
	Pos.col +=2;
	return new Symbol(sym.NOTEQ,
		new CSXToken(Pos));
}
"&&"	{
	Pos.setpos();
	Pos.col +=2;
	return new Symbol(sym.CAND,
		new CSXToken(Pos));
}
"||"	{
	Pos.setpos();
	Pos.col +=2;
	return new Symbol(sym.COR,
		new CSXToken(Pos));
}
"<"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.LT,
		new CSXToken(Pos));
}
">"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.GT,
		new CSXToken(Pos));
}
"<="	{
	Pos.setpos();
	Pos.col +=2;
	return new Symbol(sym.LEQ,
		new CSXToken(Pos));
}
">="	{
	Pos.setpos();
	Pos.col +=2;
	return new Symbol(sym.GEQ,
		new CSXToken(Pos));
}
","	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.COMMA,
		new CSXToken(Pos));
}
"!"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.NOT,
		new CSXToken(Pos));
}
"{"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.LBRACE,
		new CSXToken(Pos));
}
"}"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.RBRACE,
		new CSXToken(Pos));
}
":"	{
	Pos.setpos();
	Pos.col +=1;
	return new Symbol(sym.COLON,
			new CSXToken(Pos));
}
{INTLIT}+	{
	Pos.setpos();
	Pos.col += yytext().length();
	int i;
	final double d = Double.parseDouble(yytext());
	if (d > Integer.MAX_VALUE)
	{
		System.out.println("Error: Integer literal "+
			yytext()+" too large; replaced with "+ Integer.MAX_VALUE);
		i=Integer.MAX_VALUE;
	}
	else
	{
		i = Integer.parseInt(yytext());
	}
	return new Symbol(sym.INTLIT,
		new CSXIntLitToken(i, Pos));
}

{NEGINTLIT}+	{
	Pos.setpos();
	Pos.col += yytext().length();
	final String unsignedVal = yytext().substring(1);
	int i;
	final double d = -Double.parseDouble(unsignedVal);
	if (d < Integer.MIN_VALUE) {
		System.out.println("Error: Integer literal "+
			yytext()+" too small; replaced with "+ Integer.MIN_VALUE);
		i=Integer.MIN_VALUE;
	}
	else {
		i = -Integer.parseInt(unsignedVal);
	}
	return new Symbol(sym.INTLIT,
		new CSXIntLitToken(i, Pos));
}
{STRLIT}        {
	Pos.setpos();
	Pos.col += yytext().length();
	return new Symbol(sym.STRLIT,
		new CSXStringLitToken(yytext(), Pos));
}
{CHARLIT}       {
	Pos.setpos();
	Pos.col += yytext().length();
	final String val = yytext();
	char cval =  val.charAt(val.length()-2);
	if (val.length()==4)
	{
		if (val.charAt(2)=='n')
		{
			cval = '\n';
		}
		else if (val.charAt(2)== 't')
		{
			cval = '\t';
		}
	}
	return new Symbol(sym.CHARLIT,
			new CSXCharLitToken(cval, yytext(), Pos));
}
{COMMENT1}	{
	Pos.line +=1;
	Pos.col = 1;
}
"##"("#"?[^#])*"##"	{
	if (Pos.countEOLs(yytext()) > 0) {
		Pos.line +=Pos.countEOLs(yytext());
		Pos.col = Pos.distFromLastEOL(yytext());
	} else {
		Pos.col += yytext().length();
	}
}

(\n) | (\r\n) {
	Pos.line +=1;
	Pos.col = 1;
}

" "	{
	Pos.col +=1;
}
\t	{
	Pos.col +=1;
}
\"[^\"\n\r]*((\n)|(\r\n))
{
	Pos.setpos();
	Pos.line +=1;
	Pos.col = 1;
	String s = "runaway string: (" + yytext().substring(0, yylength() -1) + ")";

	return new Symbol(sym.error, new CSXErrorToken(s, Pos));
}
{NOTSTRING} {
	Pos.setpos();
	Pos.col += yytext().length();
	String s = "Illegal string: (" + yytext() + ")";

	return new Symbol(sym.error, new CSXErrorToken(s, Pos));
}
'[^'\n\r]*((\n)|(\r\n))
{
	Pos.setpos();
	Pos.line +=1;
	Pos.col = 1;
	String s = "runaway char: (" + yytext().substring(0, yylength() -1) + ")";

	return new Symbol(sym.error, new CSXErrorToken(s, Pos));
}
{NOTCHAR} {
	Pos.setpos();
	Pos.col += yytext().length();
	String s = "Illegal char: (" + yytext() + ")";

	return new Symbol(sym.error, new CSXErrorToken(s, Pos));
}

{NOTIDNT} {
Pos.setpos();
	Pos.col += yytext().length();
	String s = "Illegal identifier: (" + yytext() + ")";

	return new Symbol(sym.error, new CSXErrorToken(s, Pos));
}
//Print an Error Token for all other unknown scanned characters not defined previously
.
{
	Pos.setpos();
	Pos.col += yytext().length();
	String s = "invalid token: (" + yytext() + ")";

	return new Symbol(sym.error, new CSXErrorToken(s, Pos));
}

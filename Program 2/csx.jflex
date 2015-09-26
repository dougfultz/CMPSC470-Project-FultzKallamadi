//Doug Fultz
//Sujay Kallamadi
//CMPSC470 - Compiler Construction

//Subroutine definitions
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
    int linenum;            // maintain this as line number current token was scanned on
    int colnum;             // maintain this as column number current token began at
    int line;               // maintain this as line number after scanning current token
    int col;                // maintain this as column number after scanning current token
    
    Position() {
        linenum = 1;
        colnum = 1;
        line = 1;  
        col = 1;
    }
    
    void setpos() {
        // set starting position for current token
        linenum = line;
        colnum = col;
    }
} ;

//This class is used by the scanner to return token information that is useful for the parser
//This class will be replaced in our parser project by the java_cup.runtime.Symbol class
class Symbol { 
    public int sym;
    public CSXToken value;
    public Symbol(int tokenType,CSXToken theToken) {
        sym = tokenType;
        value = theToken;
    }
}

%%
//Declarations

RESERVEDWORDS=\"bool|break|char|const|continue|else|false|float|if|int|read|return|true|void|print|while\"
LETTER=[a-zA-Z]
DIGIT=[0-9]
STRLIT = \"([^\" \\ ]|\\n|\\t|\\\"|\\\\)*\"     // to be fixed

%type Symbol
%eofval{
  return new Symbol(sym.EOF,new CSXToken(0,0));
%eofval}
%{
Position Pos = new Position();
%}

%%
//Regular Expressions
/***********************************************************************
 Tokens for the CSX language are defined here using regular expressions
************************************************************************/

//-Reserved-words-------------------------------------------
//The reserved words of the CSX language.
"bool" {
    Pos.setpos();
    Pos.col+=4;
    return new Symbol(sym.rw_BOOL,new CSXToken(Pos));
}

"break" {
    Pos.setpos();
    Pos.col+=5;
    return new Symbol(sym.rw_BREAK,new CSXToken(Pos));
}

"char" {
    Pos.setpos();
    Pos.col+=4;
    return new Symbol(sym.rw_CHAR,new CSXToken(Pos));
}

"class" {
    Pos.setpos();
    Pos.col+=5;
    return new Symbol(sym.rw_CLASS,new CSXToken(Pos));
}

"const" {
    Pos.setpos();
    Pos.col+=5;
    return new Symbol(sym.rw_CONST,new CSXToken(Pos));
}

"continue" {
    Pos.setpos();
    Pos.col+=8;
    return new Symbol(sym.rw_CONTINUE,new CSXToken(Pos));
}

"else" {
    Pos.setpos();
    Pos.col+=4;
    return new Symbol(sym.rw_ELSE,new CSXToken(Pos));
}

"false" {
    Pos.setpos();
    Pos.col+=5;
    return new Symbol(sym.rw_FALSE,new CSXToken(Pos));
}

"float" {
    Pos.setpos();
    Pos.col+=5;
    //return new Symbol(sym. //TODO
}

"if" {
    Pos.setpos();
    Pos.col+=2;
    return new Symbol(sym.rw_IF,new CSXToken(Pos));
}

"int" {
    Pos.setpos();
    Pos.col+=3;
    //return new Symbol(sym. //TODO
}

"read" {
    Pos.setpos();
    Pos.col+=4;
    return new Symbol(sym.rw_READ,new CSXToken(Pos));
}

"return" {
    Pos.setpos();
    Pos.col+=6;
    return new Symbol(sym.rw_RETURN,new CSXToken(Pos));
}

"true" {
    Pos.setpos();
    Pos.col+=4;
    return new Symbol(sym.rw_TRUE,new CSXToken(Pos));
}

"void" {
    Pos.setpos();
    Pos.col+=4;
    return new Symbol(sym.rw_VOID,new CSXToken(Pos));
}

"print" {
    Pos.setpos();
    Pos.col+=5;
    //return new Symbol(sym. //TODO
}

"while" {
    Pos.setpos();
    Pos.col+=5;
    return new Symbol(sym.rw_WHILE,new CSXToken(Pos));
}

//-Identifiers----------------------------------------------
//An identifier is a sequence of letters, underscores and digits starting with a letter, excluding reserved words.
//-Integer-Literals-----------------------------------------
//An integer literal is a sequence of digits, optionally preceded by a ~. A ~ denotes a negative value.
//-Float-Literals-------------------------------------------
//A float literal is a sequence of digits that represent a decimal value, optionally preceded by a ~. A ~ denotes a negative decimal. Examples of legal float literal are: .6 and 5., 12.345, ~.7 while 5 or ~43 are not considered as legal float values.
//-String Literals------------------------------------------
//
//----------------------------------------------------------
//----------------------------------------------------------
//----------------------------------------------------------

//Addition
"+" {
    Pos.setpos();
    Pos.col += 1;
    return new Symbol(sym.PLUS,new CSXToken(Pos));
}

//Not Equal to
"!=" {
    Pos.setpos();
    Pos.col +=2;
    return new Symbol(sym.NOTEQ,new CSXToken(Pos));
}

//Semicolon
";" {
    Pos.setpos();
    Pos.col +=1;
    return new Symbol(sym.SEMI,new CSXToken(Pos));
}

//Integer
{DIGIT}+ {
    // This def doesn't check for overflow -- be sure to update it
    Pos.setpos(); 
    Pos.col += yytext().length();
    return new Symbol(sym.INTLIT,new CSXIntLitToken(Integer.parseInt(yytext()),Pos));
}

//EOL to be fixed so that it accepts different formats
//New line (UNIX)
\n {
    Pos.line += 1;
    Pos.col = 1;
}

//White space
//Single space
" " {
    Pos.col += 1;
}

//Tab

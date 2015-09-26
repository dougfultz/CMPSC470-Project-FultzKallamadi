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
//A string literal is any sequence of printable characters, delimited by double quotes. A double quote within the text of a string must be escaped (to avoid being misinterpreted as the end of the string). Tabs and newlines within a string are escaped as usual (e.g., \n is a newline and \t is a tab). Backslashes within a string must also be escaped (as \\). Strings may not cross line boundaries.
//StringLit = " ( Not(" | \ | UnprintableChars) | \" | \n | \t | \\ )* "
//-Character-Literals---------------------------------------
//A character literal is any printable ASCII character, enclosed within single quotes. A single quote within a character literal must be escaped (to avoid being misinterpreted as the end of the literal). A tab or newline must be escaped (e.g., '\n' is a newline and '\t' is a tab). A backslash must also be escaped (as '\\').
//CharLit = ' ( Not(' | \ | UnprintableChars) | \' | \n | \t | \\ ) '
//-Other Tokens---------------------------------------------
//These are miscellaneous one- or two-character symbols representing operators and delimiters.
//-++ and -- Operators--------------------------------------
//The increment (++) and decrement (--) operators should be used either before or after an identifier. There should be no space between the identifier and the operator. It might be helpful, while modifying the scanner (jflex specification), to use look-ahead and lexical states.
//-End-of-File-(EOF)-Token----------------------------------
//The EOF token is automatically returned by yylex() when it reaches the end of file while scanning the first character of a token.
//-Single-Line-Comment--------------------------------------
//As in C++ and Java, this style of comment begins with a pair of slashes and ends at the end of the current line. Its body can include any character other than an end-of-line.
//LineComment = // Not(Eol)* Eol
//-Multi-Line-Comment---------------------------------------
//This comment begins with the pair ## and ends with the pair ##. Its body can include any character sequence including # but not two consecutive #’s.
//-White-Space----------------------------------------------
//White space separates tokens; otherwise it is ignored.
//WhiteSpace = ( Blank | Tab | Eol) +
//-Error Character Token------------------------------------
//Any character that cannot be scanned as part of a valid token, comment or white space is invalid and should generate an error message.
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

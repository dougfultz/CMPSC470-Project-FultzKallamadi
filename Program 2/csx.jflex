/*  Expand this into your solution for project 2 */

/** Generic token for CSX language
 *  @author Doug Fultz
 *  @author Sujay Kallamadi
 */
class CSXToken {
    int linenum;
    int colnum;
    
    /** Generic constructor
     *  Taking zero (0) parameters.
     */
    CSXToken() {
    }
    
    /** Constructor for position as separate values
     *  @param line Location of token within text.
     *  @param col  Location of token within text.
     */
    CSXToken(int line,int col) {
        linenum = line;
        colnum = col;
    }
    
    /** Constructor for position as Position class
     *  @param p Position of token within text as Position object.
     */
    CSXToken(Position p) {
        linenum = p.linenum;
        colnum = p.colnum;
    }
}

/** Token for Integer Literals for CSX language
 */
class CSXIntLitToken extends CSXToken {
    int intValue;
    
    /** Constructor for storing value and position
     *  @param val Value of integer of the token.
     *  @param p   Position of the token within text as a Position object.
     */
    CSXIntLitToken(int val, Position p) {
       super(p);
       intValue=val; 
    }
}

/** Token for Float Literals for CSX language
 */
class CSXFloatLitToken extends CSXToken {
    // Expand - should contain floatValue
    float floatValue;
    
    /** Constructor for storing value and position
     *  @param val Value of float of the token.
     *  @param p   Position of the token within text as a Position object.
     */
    CSXFloatLitToken(float val, Position p){
        super(p);
        floatValue=val;
    }
}

/** Token for Identifiers for CSX language
 */
class CSXIdentifierToken extends CSXToken {
    // Expand - should contain identifierText
    String identifierText;
    
    /** Constructor for storing value and position
     *  @param val Value of the identifier string of the token.
     *  @param p   Position of the token within text as a Position object.
     */
    CSXIdentifierToken(String val, Position p){
        super(p);
        identifierText=val;
    }
}

/** Token for Character Literals for CSX language
 */
class CSXCharLitToken extends CSXToken {
    // Expand - should contain charValue
    char charValue;
    
    /** Constructor for storing value and position
     *  @param val Value of the character of the token.
     *  @param p   Position of the token within text as a Position object.
     */
    CSXCharLitToken(char val, Position p){
        super(p);
        charValue=val;
    }
}

/** Token for String Literals for CSX language
 */
class CSXStringLitToken extends CSXToken {
    // Expand - should contain stringText
    String stringText;
    
    /** Constructor for storing value and position
     *  @param val Value of the string of the token.
     *  @param p   Position of the token within text as a Position object.
     */
    CSXStringLitToken(String val, Position p){
        super(p);
        stringText=val;
    }
}

// This class is used to track line and column numbers
// Feel free to change to extend it
/** Class to store the position of tokens with the text
 */
class Position {
    int  linenum;             /* maintain this as line number current
                                            token was scanned on */
    int  colnum;             /* maintain this as column number current
                                            token began at */
    int  line;                 /* maintain this as line number after
                                           scanning current token  */
    int  col;                 /* maintain this as column number after
                                           scanning current token  */
    
    /** Generic constructor
     *  Taking zero (0) parameters.
     */
    Position(){
        linenum = 1;     
        colnum = 1;     
        line = 1;  
        col = 1;
    }
    
    /** Stores the current position of a token.
     */
    void setpos() {
        // set starting position for current token
        linenum = line;
        colnum = col;
    }
} ;

/** Generic class to store the symbols of the tokens
 *  This class is used by the scanner to return token information that is useful for the parser
 *  This class will be replaced in our parser project by the java_cup.runtime.Symbol class
 */
class Symbol { 
    public int sym;
    public CSXToken value;
    
    /** Constructor taking the symbol type and the token itself
     *  @param tokenType integer of the designated token.
     *  @param theToken  the token being stored.
     */
    public Symbol(int tokenType, CSXToken theToken) {
        sym = tokenType;
        value = theToken;
    }
}

%%

DIGIT=[0-9]

/** Reserved words macros
 *  The reserved words of the CSX language
 */
BOOL=[Bb][Oo][Oo][Ll]
BREAK=[Bb][Rr][Ee][Aa][Kk]
CHAR=[Cc][Hh][Aa][Rr]
CLASS=[Cc][Ll][Aa][Ss][Ss]
CONST=[Cc][Oo][Nn][Ss][Tt]
CONTINUE=[Cc][Oo][Nn][Tt][Ii][Nn][Uu][Ee]
ELSE=[Ee][Ll][Ss][Ee]
FALSE=[Ff][Aa][Ll][Ss][Ee]
FLOAT=[Ff][Ll][Oo][Aa][Tt]
IF=[Ii][Ff]
INT=[Ii][Nn][Tt]
READ=[Rr][Ee][Aa][Dd]
RETURN=[Rr][Ee][Tt][Uu][Rr][Nn]
TRUE=[Tt][Rr][Uu][Ee]
VOID=[Vv][Oo][Ii][Dd]
PRINT=[Pp][Rr][Ii][Nn][Tt]
WHILE=[Ww][Hh][Ii][Ll][Ee]
RESERVEDWORD=BOOL|BREAK|CHAR|CLASS|CONST|CONTINUE|ELSE|FALSE|FLOAT|IF|INT|READ|RETURN|TRUE|VOID|PRINT|WHILE

/** Identifier macros
 *  An identifier is a sequence of letters, underscores and digits starting
 *  with a letter, excluding reserved words.
 */
LETTER=[a-zA-Z]
ALHANUMERIC=[a-zA-Z0-9]*
IDENTIFIER={LETTER}+({ALHANUMERIC}|_)*

/** Integer Literals macros
 *  An integer literal is a sequence of digits, optionally preceded by a ~.
 *  A ~ denotes a negative value.
 */
INTEGERPOSITIVE={DIGIT}+
INTEGERNEGATIVE=~{DIGIT}
INTEGERLITERAL={INTEGERPOSITIVE}|{INTEGERNEGATIVE}

/** Float Literals macros
 *  A float literal is a sequence of digits that represent a decimal value,
 *  optionally preceded by a ~. A ~ denotes a negative decimal. Examples of
 *  legal float literal are: .6 and 5., 12.345, ~.7 while 5 or ~43 are not
 *  considered as legal float
 */
FLOATPOSITIVE=\.{DIGIT}+|{DIGIT}+\.|{DIGIT}+\.{DIGIT}+
FLOATNEGATIVE=~\.{DIGIT}+|~{DIGIT}+\.|~{DIGIT}+\.{DIGIT}+
FLOATLITERAL={FLOATPOSITIVE}|{FLOATNEGATIVE}

/** String Literals macros
 *  A string literal is any sequence of printable characters, delimited by
 *  double quotes. A double quote within the text of a string must be escaped
 *  (to avoid being misinterpreted as the end of the string). Tabs and
 *  newlines within a string are escaped as usual (e.g., \n is a newline and
 *  \t is a tab). Backslashes within a string must also be escaped (as \\).
 *  Strings may not cross line boundaries.
 *  StringLit = " ( Not(" | \ | UnprintableChars) | \" | \n | \t | \\ )* "
 */
//PRINTABLECHARACTERS=[a-zA-Z0-9`~!@#$%\^&*\(\)\-_+={\[}\]\|\\:;"'<,>.?/]
//UNPRINTABLECHARACTERS=[^a-zA-Z0-9`~!@#$%\^&*()\-_+={\[}\]\|\\:;"'<,>.?/]
//STRINGLITERAL=\"([^\"\\{UNPRINTABLECHARACTERS}]|\\\"|\\n|\\t|\\\\)*\"
//https://stackoverflow.com/questions/19502563/jflex-regular-expression
STRINGLITERAL=\"[^\"]*\"
//STRLIT = \"([^\" \\ ]|\\n|\\t|\\\"|\\\\)*\"        // to be fixed

/** Character Literals macros
 *  A character literal is any printable ASCII character, enclosed within
 *  single quotes. A single quote within a character literal must be escaped
 *  (to avoid being misinterpreted as the end of the literal). A tab or
 *  newline must be escaped (e.g., '\n' is a newline and '\t' is a tab). A
 *  backslash must also be escaped (as '\\').
 *  CharLit = ' ( Not(' | \ | UnprintableChars) | \' | \n | \t | \\ ) '
 */
CHARACTERLITEAL=\'[^\']\'

/** Other Tokens macros
 *  These are miscellaneous one- or two-character symbols representing
 *  operators and delimiters.
 */
LEFTPAREN="("
RIGHTPAREN=")"
LEFTBRACKET="["
RIGHTBRACKET="]"
ASSIGNMENT="="
SEMICOLON=";"
ADDITION="+"
SUBTRACTION="-"
MULTIPLICATION="*"
DIVISION="/"
ISEQUALTO="=="
NOTEQUALTO="!="
ANDOPERATOR="&&"
OROPERATOR="||"
LESSTHAN="<"
GREATERTHAN=">"
LESSTHANEQUAL="<="
GREATERTHANEQUAL=">="
COMMA=","
NOTOPERATOR="!"
LEFTBRACE="{"
RIGHTBRACE="}"
COLON=":"

/** Increment and Decrement macros
 *  The increment (++) and decrement (--) operators should be used either
 *  before or after an identifier. There should be no space between the
 *  identifier and the operator. It might be helpful, while modifying the
 *  scanner (jflex specification), to use look-ahead and lexical states.
 */
INCREMENT="++"
DECREMENT="--"

/** End-of-file (EOF) Token
 *  The EOF token is automatically returned by yylex() when it reaches the end
 *  of file while scanning the first character of a token.
 */
%type Symbol
%eofval{
  return new Symbol(sym.EOF, new CSXToken(0,0));
%eofval}

/** Single Line Comment macro
 *  As in C++ and Java, this style of comment begins with a pair of slashes
 *  and ends at the end of the current line. Its body can include any
 *  character other than an end-of-line.
 *  LineComment = // Not(Eol)* Eol
 */
//http://jflex.de/manual.html#Example
LINETERMINATOR=\r|\n|\r\n
INPUTCHARACTER=[^\r\n]
SINGLELINECOMMENT="//"{INPUTCHARACTER}*{LINETERMINATOR}?

/** Multi-Line Comment macro
 *  This comment begins with the pair ## and ends with
 *  the pair ##. Its body can include any character sequence including # but
 *  not two consecutive #’s.
 */
MULTILINECOMMENT="##"[^"##"]*"##"
//http://jflex.de/manual.html#Example
ALLCOMMENTS={SINGLELINECOMMENT}|{MULTILINECOMMENT}

/** White Space macro
 *  White space separates tokens; otherwise it is ignored.
 *  WhiteSpace = ( Blank | Tab | Eol) +
 */
//http://jflex.de/manual.html#Example
WHITESPACE=({LINETERMINATOR}|[ \t])+

/** Error Token macro
 *  Any character that cannot be scanned as part of a valid token, comment or
 *  white space is invalid and should generate an error message.
 */
ERRORTOKEN=.

%{
Position Pos = new Position();
%}

/** Lexical States */
%state YYIDENTIFIER

%%
/***********************************************************************
 Tokens for the CSX language are defined here using regular expressions
************************************************************************/

/** Reserved words rules
 *  The reserved words of the CSX language
 */
<YYINITIAL> {
    {BOOL} {
        Pos.setpos();
        Pos.col+=4;
        return new Symbol(sym.rw_BOOL,new CSXToken(Pos));
    }
    {BREAK} {
        Pos.setpos();
        Pos.col+=5;
        return new Symbol(sym.rw_BREAK,new CSXToken(Pos));
    }
    {CHAR} {
        Pos.setpos();
        Pos.col+=4;
        return new Symbol(sym.rw_CHAR,new CSXToken(Pos));
    }
    {CLASS} {
        Pos.setpos();
        Pos.col+=5;
        return new Symbol(sym.rw_CLASS,new CSXToken(Pos));
    }
    {CONST} {
        Pos.setpos();
        Pos.col+=5;
        return new Symbol(sym.rw_CONST,new CSXToken(Pos));
    }
    {CONTINUE} {
        Pos.setpos();
        Pos.col+=8;
        return new Symbol(sym.rw_CONTINUE,new CSXToken(Pos));
    }
    {ELSE} {
        Pos.setpos();
        Pos.col+=4;
        return new Symbol(sym.rw_ELSE,new CSXToken(Pos));
    }
    {FALSE} {
        Pos.setpos();
        Pos.col+=5;
        return new Symbol(sym.rw_FALSE,new CSXToken(Pos));
    }
    {FLOAT} {
        Pos.setpos();
        Pos.col+=5;
        return new Symbol(sym.rw_FLOAT,new CSXToken(Pos));
    }
    {IF} {
        Pos.setpos();
        Pos.col+=2;
        return new Symbol(sym.rw_IF,new CSXToken(Pos));
    }
    {INT} {
        Pos.setpos();
        Pos.col+=3;
        return new Symbol(sym.rw_INT,new CSXToken(Pos));
    }
    {READ} {
        Pos.setpos();
        Pos.col+=4;
        return new Symbol(sym.rw_READ,new CSXToken(Pos));
    }
    {RETURN} {
        Pos.setpos();
        Pos.col+=6;
        return new Symbol(sym.rw_RETURN,new CSXToken(Pos));
    }
    {TRUE} {
        Pos.setpos();
        Pos.col+=4;
        return new Symbol(sym.rw_TRUE,new CSXToken(Pos));
    }
    {VOID} {
        Pos.setpos();
        Pos.col+=4;
        return new Symbol(sym.rw_VOID,new CSXToken(Pos));
    }
    {PRINT} {
        Pos.setpos();
        Pos.col+=5;
        return new Symbol(sym.rw_PRINT,new CSXToken(Pos));
    }
    {WHILE} {
        Pos.setpos();
        Pos.col+=5;
        return new Symbol(sym.rw_WHILE,new CSXToken(Pos));
    }
}

/** Identifier rules
 *  An identifier is a sequence of letters, underscores and digits starting
 *  with a letter, excluding reserved words.
 */
<YYINITIAL> {
    {IDENTIFIER} {
        yybegin(YYIDENTIFIER);
        Pos.setpos();
        Pos.col+=yytext().length();
        return new Symbol(sym.IDENTIFIER,new CSXIdentifierToken(yytext(),Pos));
    }
}

/** Integer Literals rules
 *  An integer literal is a sequence of digits, optionally preceded by a ~.
 *  A ~ denotes a negative value.
 */
<YYINITIAL> {
    {INTEGERPOSITIVE} {
        //TODO test overflow code
        Pos.setpos();
        Pos.col += yytext().length();
        //Parse Integer
        try{
            return new Symbol(sym.INTLIT,new CSXIntLitToken(Integer.parseInt(yytext()),Pos));
        }catch(NumberFormatException e){
            return new Symbol(sym.error,new CSXToken(Pos));
        }
    }
    {INTEGERNEGATIVE} {
        //TODO test overflow code
        Pos.setpos();
        Pos.col+=yytext().length();
        try{
            return new Symbol(sym.INTLIT,new CSXIntLitToken(Integer.parseInt("-"+yytext().substring(1)),Pos));
        }catch(NumberFormatException e){
            return new Symbol(sym.error,new CSXToken(Pos));
        }
    }
}

/** Float Literals rules
 *  A float literal is a sequence of digits that represent a decimal value,
 *  optionally preceded by a ~. A ~ denotes a negative decimal. Examples of
 *  legal float literal are: .6 and 5., 12.345, ~.7 while 5 or ~43 are not
 *  considered as legal float
 */
<YYINITIAL> {
    {FLOATPOSITIVE} {
        //TODO test overflow code
        Pos.setpos();
        Pos.col+=yytext().length();
        try{
            return new Symbol(sym.FLOATLIT,new CSXFloatLitToken(Float.parseFloat(yytext()),Pos));
        }catch(NumberFormatException e){
            return new Symbol(sym.error,new CSXToken(Pos));
        }
    }
    {FLOATNEGATIVE} {
        //TODO test overflow code
        Pos.setpos();
        Pos.col+=yytext().length();
        try{
            return new Symbol(sym.FLOATLIT,new CSXFloatLitToken(Float.parseFloat("-"+yytext().substring(1)),Pos));
        }catch(NumberFormatException e){
            return new Symbol(sym.error,new CSXToken(Pos));
        }
    }
}

/** String Literals rules
 *  A string literal is any sequence of printable characters, delimited by
 *  double quotes. A double quote within the text of a string must be escaped
 *  (to avoid being misinterpreted as the end of the string). Tabs and
 *  newlines within a string are escaped as usual (e.g., \n is a newline and
 *  \t is a tab). Backslashes within a string must also be escaped (as \\).
 *  Strings may not cross line boundaries.
 *  StringLit = " ( Not(" | \ | UnprintableChars) | \" | \n | \t | \\ )* "
 */
<YYINITIAL> {
    {STRINGLITERAL} {
        Pos.setpos();
        Pos.col+=yytext().length();
        return new Symbol(sym.STRLIT,new CSXStringLitToken(yytext().substring(1,yytext().length()-1),Pos));
    }
}

/** Character Literals rules
 *  A character literal is any printable ASCII character, enclosed within
 *  single quotes. A single quote within a character literal must be escaped
 *  (to avoid being misinterpreted as the end of the literal). A tab or
 *  newline must be escaped (e.g., '\n' is a newline and '\t' is a tab). A
 *  backslash must also be escaped (as '\\').
 *  CharLit = ' ( Not(' | \ | UnprintableChars) | \' | \n | \t | \\ ) '
 */
<YYINITIAL> {
    {CHARACTERLITEAL} {
        Pos.setpos();
        Pos.col+=yytext().length();
        return new Symbol(sym.CHARLIT,new CSXCharLitToken(yytext().substring(1,yytext().length()-1),Pos));
    }
}

/** Other Tokens rules
 *  These are miscellaneous one- or two-character symbols representing
 *  operators and delimiters.
 */
<YYINITIAL> {
    {LEFTPAREN} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.LPAREN,new CSXToken(Pos));
    }
    {RIGHTPAREN} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.RPAREN,new CSXToken(Pos));
    }
    {LEFTBRACKET} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.LBRACKET,new CSXToken(Pos));
    }
    {RIGHTBRACKET} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.RBRACKET,new CSXToken(Pos));
    }
    {ASSIGNMENT} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.ASG,new CSXToken(Pos));
    }
    {SEMICOLON} {
        Pos.setpos();
        Pos.col +=1;
        return new Symbol(sym.SEMI,new CSXToken(Pos));
    }
    {ADDITION} {
        Pos.setpos();
        Pos.col += 1;
        return new Symbol(sym.PLUS,new CSXToken(Pos));
    }
    {SUBTRACTION} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.MINUS,new CSXToken(Pos));
    }
    {MULTIPLICATION} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.TIMES,new CSXToken(Pos));
    }
    {DIVISION} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.SLASH,new CSXToken(Pos));
    }
    {ISEQUALTO} {
        Pos.setpos();
        Pos.col+=2;
        return new Symbol(sym.EQ,new CSXToken(Pos));
    }
    {NOTEQUALTO} {
        Pos.setpos();
        Pos.col +=2;
        return new Symbol(sym.NOTEQ,new CSXToken(Pos));
    }
    {ANDOPERATOR} {
        Pos.setpos();
        Pos.col+=2;
        return new Symbol(sym.CAND,new CSXToken(Pos));
    }
    {OROPERATOR} {
        Pos.setpos();
        Pos.col+=2;
        return new Symbol(sym.COR,new CSXToken(Pos));
    }
    {LESSTHAN} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.LT,new CSXToken(Pos));
    }
    {GREATERTHAN} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.GT,new CSXToken(Pos));
    }
    {LESSTHANEQUAL} {
        Pos.setpos();
        Pos.col+=2;
        return new Symbol(sym.LEQ,new CSXToken(Pos));
    }
    {GREATERTHANEQUAL} {
        Pos.setpos();
        Pos.col+=2;
        return new Symbol(sym.GEQ,new CSXToken(Pos));
    }
    {COMMA} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.COMMA,new CSXToken(Pos));
    }
    {NOTOPERATOR} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.NOT,new CSXToken(Pos));
    }
    {LEFTBRACE} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.LBRACE,new CSXToken(Pos));
    }
    {RIGHTBRACE} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.RBRACE,new CSXToken(Pos));
    }
    {COLON} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.COLON,new CSXToken(Pos));
    }
}

/** Increment and Decrement rules
 *  The increment (++) and decrement (--) operators should be used either
 *  before or after an identifier. There should be no space between the
 *  identifier and the operator. It might be helpful, while modifying the
 *  scanner (jflex specification), to use look-ahead and lexical states.
 */
<YYINITIAL, YYIDENTIFIER> {
    {INCREMENT} {
        Pos.setpos();
        Pos.col+=2;
        return new Symbol(sym.INC,new CSXToken(Pos));
    }
    {DECREMENT} {
        Pos.setpos();
        Pos.col+=2;
        return new Symbol(sym.DEC,new CSXToken(Pos));
    }
}

/** Single Line Comment rules
 *  As in C++ and Java, this style of comment begins with a pair of slashes
 *  and ends at the end of the current line. Its body can include any
 *  character other than an end-of-line.
 *  LineComment = // Not(Eol)* Eol
 */
<YYINITIAL> {
    {SINGLELINECOMMENT} {
        Pos.setpos();
        Pos.col=1;
        Pos.line+=1;
    }
}

/** Multi-Line Comment rules
 *  This comment begins with the pair ## and ends with
 *  the pair ##. Its body can include any character sequence including # but
 *  not two consecutive #’s.
 */
<YYINITIAL> {
    {MULTILINECOMMENT} {
        Pos.setpos();
        String[] comment=yytext().split("\r|\n|\r\n");
        Pos.line+=comment.length();
        Pos.col=comment[comment.length()-1].length();
    }
}

/** White Space rules
 *  White space separates tokens; otherwise it is ignored.
 *  WhiteSpace = ( Blank | Tab | Eol) +
 */
<YYINITIAL> {
    {WHITESPACE} {
        Pos.setpos();
        String[] whitespace=yytext.split("\r|\n|\r\n");
        if(whitespace.length()==1){
            Pos.col+=yytext.length();
        }else{
            Pos.line+=comment.length();
            Pos.col=comment[comment.length()-1].length();
        }
    }
}

/** Error Token rules
 *  Any character that cannot be scanned as part of a valid token, comment or
 *  white space is invalid and should generate an error message.
 */
<YYINITIAL> {
    {ERRORTOKEN} {
        Pos.setpos();
        Pos.col+=1;
        return new Symbol(sym.error,new CSXToken(Pos));
    }
}

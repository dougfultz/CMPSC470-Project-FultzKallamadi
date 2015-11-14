import java_cup.runtime.*;
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
	float floatValue;
	CSXFloatLitToken (float val, Position p)
	{
		super(p);
		floatValue=val;
	}
}

class CSXIdentifierToken extends CSXToken {
	String identifierText;
	CSXIdentifierToken(String text, Position p)
	{
		super(p);
		identifierText=text;
	}
}

class CSXCharLitToken extends CSXToken {
	String charValue;
	CSXCharLitToken(String val, Position p)
	{
		super(p);
		charValue=val;
	}
}

class CSXStringLitToken extends CSXToken {
	String stringValue;
	CSXStringLitToken(String val, Position p)
	{
		super(p);
		stringValue=val;
	}
}

// Class that represents a token for various errors
class CSXErrorToken extends CSXToken {
	// description of the error
	String errorMessage;
	CSXErrorToken(String msg, Position p)
	{
		super(p);
		errorMessage=msg;
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
} ;




%%

%ignorecase

// REGEX to match reserved words
RESERVED = (bool)|(break)|(char)|(class)|(const)|(continue)|(else)|(false)|(if)|(int)|(read)|(return)|(true)|(void)|(print)|(while)

// REGEX to match valid and invalid identifiers
IDENTIFIER = [a-zA-Z][a-zA-Z0-9_]*
INVALID_IDENTIFIER = _[a-zA-Z0-9_]*

// REGEX to match miscellaneous operators/symbols
MISC_TOKEN = (\()|(\))|(\[)|(\])|(\=)|(\;)|(\+)|(\-)|(\*)|(\/)|(\=\=)|(\!\=)|(\&\&)|(\|\|)|(\<)|(\>)|(\<\=)|(\>\=)|(\,)|(\!)|(\{)|(\})|(\:)|(\+\+)|(\-\-)

// REGEX to match integer literals 
INTLIT = \~?[0-9]+

// REGEX to match single and multiline comments
SCOMMENT = \/\/([^\r\n])*
MCOMMENT = ##([^#]|#[^#])*##

// REGEX to match whitespace (tabs and space) and newline/carriage return characters
WHITESPACE = (\t|\040)
NEWLINE = \r|\n|\r\n

// REGEX that matches any other character as invalid
INVALID = .

%type Symbol
%eofval{
  return new Symbol(sym.EOF, new CSXToken(0,0));
%eofval}
%{
boolean invalidCharacter = false, invalidString = false;
Position Pos = new Position();
StringBuilder sb = new StringBuilder();
%}

// states for string literals, character literals, and post operation (++/--)
%state CHARACTER, POSTOP, STRING

%%
/***********************************************************************
 Tokens for the CSX language are defined here using regular expressions
************************************************************************/

<YYINITIAL> {

	// match and return reserved words
	{RESERVED} {
		Pos.setpos();
		Pos.col += yytext().length();

		int symbolConstant = sym.error;

		switch (yytext().toLowerCase()) {
			case "bool":
				symbolConstant = sym.rw_BOOL; break;
			case "break":
				symbolConstant = sym.rw_BREAK; break;
			case "char":
				symbolConstant = sym.rw_CHAR; break;
			case "class":
				symbolConstant = sym.rw_CLASS; break;
			case "const":
				symbolConstant = sym.rw_CONST; break;
			case "continue":
				symbolConstant = sym.rw_CONTINUE; break;
			case "else":
				symbolConstant = sym.rw_ELSE; break;
			case "false":
				symbolConstant = sym.rw_FALSE; break;
			case "if":
				symbolConstant = sym.rw_IF; break;
			case "int":
				symbolConstant = sym.rw_INT; break;
			case "read":
				symbolConstant = sym.rw_READ; break;
			case "return":
				symbolConstant = sym.rw_RETURN; break;
			case "true":
				symbolConstant = sym.rw_TRUE; break;
			case "void":
				symbolConstant = sym.rw_VOID; break;
			case "print":
				symbolConstant = sym.rw_PRINT; break;
			case "while":
				symbolConstant = sym.rw_WHILE; break;
		}

		return new Symbol(symbolConstant,
			new CSXToken(Pos));
	}

	// match and return identifier that may be followed by a ++/-- operation
	[a-zA-Z][a-zA-Z0-9_]*/(\+\+)|(\-\-) {
		yybegin(POSTOP);

		Pos.setpos();
		Pos.col += yytext().length();
		return new Symbol(sym.IDENTIFIER,
			new CSXIdentifierToken(yytext(),
				Pos));
	}

	// match and return a normal identifier 
	{IDENTIFIER} {
		Pos.setpos();
		Pos.col += yytext().length();
		return new Symbol(sym.IDENTIFIER,
			new CSXIdentifierToken(yytext(),
				Pos));
	}

	// match and return an invalid identifier
	{INVALID_IDENTIFIER} {
		Pos.setpos();
		Pos.col += yytext().length();
		return new Symbol(sym.error,
			new CSXErrorToken("Illegal identifier: (" + yytext() + ")",
				Pos));
	}

	// match and return a ++ operator followed by an identifier
	\+\+/[a-zA-Z][a-zA-Z0-9_]* {
		Pos.setpos();
		Pos.col += yytext().length();

		return new Symbol(sym.INC,
			new CSXToken(Pos));
	}

	// match and return a -- operator followed by an identifier
	\-\-/[a-zA-Z][a-zA-Z0-9_]* {
		Pos.setpos();
		Pos.col += yytext().length();

		return new Symbol(sym.DEC,
			new CSXToken(Pos));
	}

	// match and return any of the miscellaneous tokens/operators
	{MISC_TOKEN} {
		Pos.setpos();
		Pos.col += yytext().length();

		int symbolConstant = sym.error;

		switch (yytext()) {
			case "(":
				symbolConstant = sym.LPAREN; break;
			case ")":
				symbolConstant = sym.RPAREN; break;
			case "[":
				symbolConstant = sym.LBRACKET; break;
			case "]":
				symbolConstant = sym.RBRACKET; break;
			case "=":
				symbolConstant = sym.ASG; break;
			case ";":
				symbolConstant = sym.SEMI; break;
			case "+":
				symbolConstant = sym.PLUS; break;
			case "-":
				symbolConstant = sym.MINUS; break;
			case "*":
				symbolConstant = sym.TIMES; break;
			case "/":
				symbolConstant = sym.SLASH; break;
			case "==":
				symbolConstant = sym.EQ; break;
			case "!=":
				symbolConstant = sym.NOTEQ; break;
			case "&&":
				symbolConstant = sym.CAND; break;
			case "||":
				symbolConstant = sym.COR; break;
			case "<":
				symbolConstant = sym.LT; break;
			case ">":
				symbolConstant = sym.GT; break;
			case "<=":
				symbolConstant = sym.LEQ; break;
			case ">=":
				symbolConstant = sym.GEQ; break;
			case ",":
				symbolConstant = sym.COMMA; break;
			case "!":
				symbolConstant = sym.NOT; break;
			case "{":
				symbolConstant = sym.LBRACE; break;
			case "}":
				symbolConstant = sym.RBRACE; break;
			case ":":
				symbolConstant = sym.COLON; break;
			case "++":
				symbolConstant = sym.INC;
				return new Symbol(sym.error,
					new CSXErrorToken("Increment without Identifier",
						Pos));
			case "--":
				symbolConstant = sym.DEC;
				return new Symbol(sym.error,
					new CSXErrorToken("Decrement without Identifier",
						Pos));
		}

		return new Symbol(symbolConstant,
			new CSXToken(Pos));
	}
	
	// match and return an integer literal
	{INTLIT} {
		Pos.setpos();
		Pos.col += yytext().length();
		
		// check for negative value
		Double value = yytext().charAt(0) == '~' ?
			-1 * new Double(yytext().substring(1)) :
			new Double(yytext());

		int boundedValue = value.intValue();

		// check for overflow/underflow
		if(value.longValue() < Integer.MIN_VALUE) {
			boundedValue = Integer.MIN_VALUE;
			return new Symbol(sym.error,
				new CSXErrorToken("Overflow: (" + boundedValue + ")",
					Pos));
		} else if(value.longValue() > Integer.MAX_VALUE) {
			boundedValue = Integer.MAX_VALUE;
			return new Symbol(sym.error,
				new CSXErrorToken("Overflow: (" + boundedValue + ")",
					Pos));
		}

		return new Symbol(sym.INTLIT,
			new CSXIntLitToken(boundedValue,
				Pos));
	}

	// match a double quote to start the string matching
	\" {
		sb.setLength(0);
		yybegin(STRING);
	}

	// match a single quote to start character matching
	\' {
		sb.setLength(0);
		yybegin(CHARACTER);
	}

	// match and skip a multiline comment
	{MCOMMENT} {
		Pos.setpos();
		Pos.col += yytext().length();

		String match = yytext();

		int lastIndex = 0;
		int newlineCount = 0;

		while(lastIndex != -1) {
			lastIndex = match.indexOf("\n", lastIndex);

			if(lastIndex != -1) {
				newlineCount++;
				lastIndex += 2;
			}
		}

		Pos.line += newlineCount;
	}

	// match and skip a songle line comment
	{SCOMMENT} {
		Pos.setpos();
	}

	// match and skip a new line
	{NEWLINE} {
		Pos.line += 1;
		Pos.col = 1;
	}

	// match and skip any whitespace
	{WHITESPACE} {
		Pos.col += 1;
	}

	// match and return invalid characters
	{INVALID} {
		Pos.setpos();
		Pos.col += yytext().length();
		return new Symbol(sym.error,
			new CSXErrorToken("invalid token: (" + yytext() + ")",
				Pos));
	}
}

// match and return valid use of ++ post operator
<POSTOP> \+\+ {
	yybegin(YYINITIAL);

	Pos.setpos();
	Pos.col += yytext().length();

	return new Symbol(sym.INC,
		new CSXToken(Pos));
}

// match and return valid use of -- post operator
<POSTOP> \-\- {
	yybegin(YYINITIAL);

	Pos.setpos();
	Pos.col += yytext().length();

	return new Symbol(sym.DEC,
		new CSXToken(Pos));
}

// match string literals and return:
// 1. valid string literal
// 2. invalid string literal (contains invalid characters)
// 3. runaway string literal
<STRING> {
	\" {
		yybegin(YYINITIAL);
		Pos.setpos();
		Pos.col += sb.length();
		
		// if string contains invalid characters return an error token, else return a string literal token
		if (invalidString) {
			invalidString = false;

			return new Symbol(sym.error,
				new CSXErrorToken("Invalid string: (\"" + sb.toString() + "\")",
					Pos));
		} else {
			return new Symbol(sym.STRLIT,
				new CSXStringLitToken(sb.toString(),
					Pos));
		}
	}

	// match any one of the unprintable characters
	\x00|\x01|\x02|\x03|\x04|\x05|\x06|\x07|\x08|\x09|\x0B|\x0C|\x0E|\x0F|\x10|\x11|\x12|\x13|\x14|\x15|\x16|\x17|\x18|\x19|\x1A|\x1B|\x1C|\x1D|\x1E|\x1F|\x7F|\x5C {
		invalidString = true;
		sb.append(yytext());
	}

	// match valid escape sequences
	(\\t)|(\\n)|(\\r)|(\\\")|(\\\\) {
		sb.append(yytext());
	}

	// match any valid character
	[^\r\n\"\x00|\x01|\x02|\x03|\x04|\x05|\x06|\x07|\x08|\x09|\x0B|\x0C|\x0E|\x0F|\x10|\x11|\x12|\x13|\x14|\x15|\x16|\x17|\x18|\x19|\x1A|\x1B|\x1C|\x1D|\x1E|\x1F|\x7F|\x5C]+ {
		sb.append(yytext());
	}

	// matches newlines, returns error token of runaway string
	\r|\n|\r\n {
		yybegin(YYINITIAL);
		Pos.setpos();
		Pos.line += 1;
		Pos.col = 1;
		return new Symbol(sym.error,
			new CSXErrorToken("runaway string: (\"" + sb.toString() + ")",
				Pos));
	}
}

// match character literals and return:
// 1. valid character literal
// 2. runaway character literal
// 3. invalid character literal
<CHARACTER> {
	\' {
		yybegin(YYINITIAL);
		Pos.setpos();
		Pos.col += sb.length();

		// if character literal contains an invalid (unprintable) character return an error token, else return a character literal token 
		if (invalidCharacter) {
			invalidCharacter = false;

			return new Symbol(sym.error,
				new CSXErrorToken("Invalid character: (\'" + sb.toString() + "\')",
					Pos));
		} else {
			return new Symbol(sym.CHARLIT,
				new CSXCharLitToken(sb.toString(),
					Pos));
		}
	}

	// match any one of the invalid/unprintable characters
	\x00|\x01|\x02|\x03|\x04|\x05|\x06|\x07|\x08|\x09|\x0B|\x0C|\x0E|\x0F|\x10|\x11|\x12|\x13|\x14|\x15|\x16|\x17|\x18|\x19|\x1A|\x1B|\x1C|\x1D|\x1E|\x1F|\x7F|\x5C {
		invalidCharacter = true;
		sb.append(yytext());
	}

	// match valid escape sequence characters
	(\\t)|(\\n)|(\\r)|(\\\')|(\\\\) {
		sb.append(yytext());
		if(sb.length() > 2) {
			invalidCharacter = true;
		}
	}

	// match any character, if there are more than one character, set invalid literal to true
	[^\r\n\'\x00|\x01|\x02|\x03|\x04|\x05|\x06|\x07|\x08|\x09|\x0B|\x0C|\x0E|\x0F|\x10|\x11|\x12|\x13|\x14|\x15|\x16|\x17|\x18|\x19|\x1A|\x1B|\x1C|\x1D|\x1E|\x1F|\x7F|\x5C] {
		sb.append(yytext());
		if(sb.length() > 1) {
			invalidCharacter = true;
		}
	}

	// match newlines within the single quotes, return error token as runaway character literal
	\r|\n|\r\n {
		yybegin(YYINITIAL);
		Pos.setpos();
		Pos.line += 1;
		Pos.col = 1;
		return new Symbol(sym.error,
			new CSXErrorToken("runaway character: (\'" + sb.toString() + ")",
				Pos));
	}
}
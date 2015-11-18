// abstract superclass; only subclasses are actually created
abstract class ASTNode {

	int linenum;
	int colnum;

	static int typeErrors = 0; // Total number of type errors found 

	static void genIndent(int indent) {
		for (int i = 1; i<=indent; i++) {
			System.out.print("\t");
		}
	} // genIndent
	static void mustBe(boolean assertion) {
		if (! assertion) {
			throw new RuntimeException();
		}
	} // mustBe
	static void typeMustBe(int testType,int requiredType,String errorMsg) {
		if ((testType != Types.Error) && (testType != requiredType)) {
			System.out.println(errorMsg);
			typeErrors++;
		}
	} // typeMustBe
	String error() {
		return "Error (line " + linenum + "): ";
	} // error

	public static SymbolTable st = new SymbolTable();

	ASTNode() {linenum=-1; colnum=-1;}
	ASTNode(int l, int c) {linenum = l;colnum=c;}
	boolean isNull() {return false;} // Is this node null?
	
	void Unparse(int indent) {}
		// This will normally need to be redefined in a subclass
	
	void checkTypes() {}
		// This will normally need to be redefined in a subclass
} // abstract class ASTNode

// This class def probably doesn't need to be changed
class nullNode extends ASTNode {
	nullNode() {super();}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
} // class nullNode

// This node is used to root only CSX lite programs 
class csxLiteNode extends ASTNode {
	
	csxLiteNode(fieldDeclsNode decls, stmtsNode stmts, int line, int col) {
		super(line,col);
		fields = decls;
		progStmts = stmts;
	} // csxLiteNode

	void Unparse(int indent) {
		System.out.println(linenum + ":" + " {");
		fields.Unparse(1);
		progStmts.Unparse(1);
		System.out.println(linenum + ":" + " } EOF");
	} // Unparse
	void checkTypes() {
		fields.checkTypes();
		progStmts.checkTypes();
	} // checkTypes
	boolean isTypeCorrect() {
		st.openScope();
		checkTypes();
		return (typeErrors == 0);
	} // isTypeCorrect

	private final stmtsNode progStmts;
	private final fieldDeclsNode fields;
} // class csxLiteNode

// Root of all ASTs for CSX
class classNode extends ASTNode {
	classNode(identNode id, memberDeclsNode m, int line, int col) {
		super(line,col);
		className = id;
		members = m;
	} // classNode
	boolean isTypeCorrect() {return true;} // You need to refine this one
	private final identNode className;
	private final memberDeclsNode members;
} // class classNode

class memberDeclsNode extends ASTNode {
	memberDeclsNode(fieldDeclsNode f, methodDeclsNode m, int line, int col) {
		super(line,col);
		fields = f;
		methods = m;
	} // memberDeclsNode
	fieldDeclsNode fields;
	private final methodDeclsNode methods;
} // memberDeclsNode

class fieldDeclsNode extends ASTNode {
	fieldDeclsNode() {super();}
	fieldDeclsNode(declNode d, fieldDeclsNode f, int line, int col) {
		super(line,col);
		thisField = d;
		moreFields = f;
	} // fieldDeclsNode
	void Unparse(int indent) {
		thisField.Unparse(indent);
		moreFields.Unparse(indent);
	} // Unparse 
	void checkTypes() {
		thisField.checkTypes();
		moreFields.checkTypes();
	} // checkTypes 

	static nullFieldDeclsNode NULL = new nullFieldDeclsNode();
	private declNode thisField;
	private fieldDeclsNode moreFields;
} // fieldDeclsNode

class nullFieldDeclsNode extends fieldDeclsNode {
	nullFieldDeclsNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // nullFieldDeclsNode

// abstract superclass; only subclasses are actually created
abstract class declNode extends ASTNode {
	declNode() {super();}
	declNode(int l,int c) {super(l,c);}
} // abstract class declNode

class varDeclNode extends declNode {
	varDeclNode(identNode id, typeNode t, exprNode e, int line, int col) {
		super(line,col);
		varName = id;
		varType = t;
		initValue = e;
	} // varDeclNode
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		varType.Unparse(0);
		System.out.print(" ");
		varName.Unparse(0);
		System.out.println(";");
	} // Unparse

	void checkTypes() {
		SymbolInfo id;
		id = (SymbolInfo) st.localLookup(varName.idname);
		if (id == null) {
			id = new SymbolInfo(varName.idname,
				new Kinds(Kinds.Var),varType.type);
			varName.type = varType.type;
			try {
				st.insert(id);
			} catch (DuplicateException d) {
				/* can't happen */
			} catch (EmptySTException e) {
				/* can't happen */
			}
			varName.idinfo = id;
		} else {
			System.out.println(error() + id.name() + " is already declared.");
			typeErrors++;
			varName.type = new Types(Types.Error);
		} // id != null
	} // checkTypes

	private final identNode varName;
	private final typeNode varType;
	private final exprNode initValue;
} // class varDeclNode

class constDeclNode extends declNode {
	constDeclNode(identNode id, exprNode e,
			int line, int col) {
		super(line,col);
		constName = id;
		constValue = e;
	} // constDeclNode

	private final identNode constName;
	private final exprNode constValue;
}


class arrayDeclNode extends declNode {
	arrayDeclNode(identNode id, typeNode t, intLitNode lit,
			int line, int col) {
		super(line,col);
		arrayName = id;
		elementType = t;
		arraySize = lit;
	} // arrayDeclNode

	private final identNode arrayName;
	private final typeNode elementType;
	private final intLitNode arraySize;
} // class arrayDeclNode

abstract class typeNode extends ASTNode {
// abstract superclass; only subclasses are actually created
	typeNode() {super();}
	typeNode(int l,int c, Types t) {super(l,c);type = t;}
	static nullTypeNode NULL = new nullTypeNode();
	Types type; // Used for typechecking -- the type of this typeNode
} // abstract class typeNode

class nullTypeNode extends typeNode {
	nullTypeNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullTypeNode 

class intTypeNode extends typeNode {
	intTypeNode(int line, int col) {
		super(line,col, new Types(Types.Integer));
	} // intTypeNode
	void Unparse(int indent) {
		System.out.print("int");
	} // Unparse
	void checkTypes() {
		// No type checking needed
	} // checkTypes
} // class intTypeNode 

class boolTypeNode extends typeNode {
	boolTypeNode(int line, int col) {
		super(line,col, new Types(Types.Boolean));
	} // boolTypeNode
	void Unparse(int indent) {
		System.out.print("bool");
	} // Unparse
	void checkTypes() {
		// No type checking needed
	} // checkTypes
} // class boolTypeNode 

class charTypeNode extends typeNode {
	charTypeNode(int line, int col) {
		super(line,col, new Types(Types.Character));
	} // charTypeNode
	void checkTypes() {
		// No type checking needed
	} // checkTypes
} // class charTypeNode 

class voidTypeNode extends typeNode {
	voidTypeNode(int line, int col) {
		super(line,col, new Types(Types.Void));
	} // voidTypeNode
	void checkTypes() {
		// No type checking needed
	} // checkTypes
} // class voidTypeNode 

class methodDeclsNode extends ASTNode {
	methodDeclsNode() {super();}
	methodDeclsNode(methodDeclNode m, methodDeclsNode ms, int line, int col) {
		super(line,col);
		thisDecl = m;
		moreDecls = ms;
	} // methodDeclsNode
	static nullMethodDeclsNode NULL = new nullMethodDeclsNode();
	private methodDeclNode thisDecl;
	private methodDeclsNode moreDecls;
} // class methodDeclsNode 

class nullMethodDeclsNode extends methodDeclsNode {
	nullMethodDeclsNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullMethodDeclsNode 

class methodDeclNode extends ASTNode {
	methodDeclNode(identNode id, argDeclsNode a, typeNode t, fieldDeclsNode f,
			stmtsNode s, int line, int col) {
		super(line,col);
		name = id;
		args = a;
		returnType = t;
		decls = f;
		stmts = s;
	} // methodDeclNode

	private final identNode name;
	private final argDeclsNode args;
	private final typeNode returnType;
	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class methodDeclNode 

// abstract superclass; only subclasses are actually created
abstract class argDeclNode extends ASTNode {
	argDeclNode() {super();}
	argDeclNode(int l,int c) {super(l,c);}
} // abstract class argDeclNode

class argDeclsNode extends ASTNode {
	argDeclsNode() {}
	argDeclsNode(argDeclNode arg, argDeclsNode args, int line, int col) {
		super(line,col);
		thisDecl = arg;
		moreDecls = args;
	} // argDeclsNode
	static nullArgDeclsNode NULL = new nullArgDeclsNode();

	private argDeclNode thisDecl;
	private argDeclsNode moreDecls;
} // class argDeclsNode 

class nullArgDeclsNode extends argDeclsNode {
	nullArgDeclsNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullArgDeclsNode 

class arrayArgDeclNode extends argDeclNode {
	arrayArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line,col);
		argName = id;
		elementType = t;
	} // arrayArgDeclNode

	private final identNode argName;
	private final typeNode elementType;
} // class arrayArgDeclNode 

class valArgDeclNode extends argDeclNode {
	valArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line,col);
		argName = id;
		argType = t;
	} // valArgDeclNode

	private final identNode argName;
	private final typeNode argType;
} // class valArgDeclNode 

// abstract superclass; only subclasses are actually created
abstract class stmtNode extends ASTNode {
	stmtNode() {super();}
	stmtNode(int l,int c) {super(l,c);}
	static nullStmtNode NULL = new nullStmtNode();
} // abstract class stmtNode

class nullStmtNode extends stmtNode {
	nullStmtNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullStmtNode 

class stmtsNode extends ASTNode {
	stmtsNode(stmtNode stmt, stmtsNode stmts, int line, int col) {
		super(line,col);
		thisStmt = stmt;
		moreStmts = stmts;
	} // stmtsNode
	stmtsNode() {}

	void Unparse(int indent) {
		thisStmt.Unparse(indent);
		moreStmts.Unparse(indent);
	} // Unparse 
	void checkTypes() {
		thisStmt.checkTypes();
		moreStmts.checkTypes();
	} // checkTypes 

	static nullStmtsNode NULL = new nullStmtsNode();
	private stmtNode thisStmt;
	private stmtsNode moreStmts;
} // class stmtsNode 

class nullStmtsNode extends stmtsNode {
	nullStmtsNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullStmtsNode 

class asgNode extends stmtNode {
	asgNode(nameNode n, exprNode e, int line, int col) {
		super(line,col);
		target = n;
		source = e;
	} // asgNode

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		target.Unparse(0);
		System.out.print(" = ");
		source.Unparse(0);
		System.out.println(";");
	} // Unparse

	void checkTypes() {
		target.checkTypes();
		source.checkTypes();
		mustBe(target.kind.val == Kinds.Var); //In CSX-lite all IDs should be vars! 
		typeMustBe(source.type.val, target.type.val,
			error() + "Both the left and right"
			+ " hand sides of an assignment must "
			+ "have the same type.");
	} // checkTypes

	private final nameNode target;
	private final exprNode source;
} // class asgNode 

class ifThenNode extends stmtNode {
	ifThenNode(exprNode e, stmtNode s1, stmtNode s2, int line, int col) {
		super(line,col);
		condition = e;
		thenPart = s1;
		elsePart = s2;
	} // ifThenNode

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("if (");
		condition.Unparse(0);
		System.out.println(")");
		thenPart.Unparse(indent+1);
		// No else parts in CSX Lite
	} // Unparse

	void checkTypes() {
		condition.checkTypes();
		typeMustBe(condition.type.val, Types.Boolean,
			error() + "The control expression of an" +
			" if must be a bool.");
		thenPart.checkTypes();
		// No else parts in CSX Lite
	} // checkTypes

	private final exprNode condition;
	private final stmtNode thenPart;
	private final stmtNode elsePart;
} // class ifThenNode 

class whileNode extends stmtNode {
	whileNode(identNode i, exprNode e, stmtNode s, int line, int col) {
		super(line,col);
		label = i;
		condition = e;
		loopBody = s;
	} // whileNode

	private final exprNode label;
	private final exprNode condition;
	private final stmtNode loopBody;
} // class whileNode 

class readNode extends stmtNode {
	readNode() {}
	readNode(nameNode n, readNode rn, int line, int col) {
		super(line,col);
		targetVar = n;
		moreReads = rn;
	} // readNode

	static nullReadNode NULL = new nullReadNode();
	private nameNode targetVar;
	private readNode moreReads;
} // class readNode 

class nullReadNode extends readNode {
	nullReadNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullReadNode 

class printNode extends stmtNode {
	printNode() {}
	printNode(exprNode val, printNode pn, int line, int col) {
		super(line,col);
		outputValue = val;
		morePrints = pn;
	} // printNode
	static nullPrintNode NULL = new nullPrintNode();
	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("print(");
		outputValue.Unparse(0);
		System.out.println(");");
	} // Unparse

	void checkTypes() {
		outputValue.checkTypes();
		typeMustBe(outputValue.type.val, Types.Integer,
			error() + "Only int values may be printed.");
	} // checkTypes

	private exprNode outputValue;
	private printNode morePrints;
} // class printNode 

class nullPrintNode extends printNode {
	nullPrintNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullPrintNode 

class callNode extends stmtNode {
	callNode(identNode id, argsNode a, int line, int col) {
		super(line,col);
		methodName = id;
		args = a;
	} // callNode

	private final identNode methodName;
	private final argsNode args;
} // class callNode 

class returnNode extends stmtNode {
	returnNode(exprNode e, int line, int col) {
		super(line,col);
		returnVal = e;
	} // returnNode

	private final exprNode returnVal;
} // class returnNode 

class blockNode extends stmtNode {
	blockNode(fieldDeclsNode f, stmtsNode s, int line, int col) {
		super(line,col);
		decls = f;
		stmts = s;
	} // blockNode

	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class blockNode 

class breakNode extends stmtNode {
	breakNode(identNode i, int line, int col) {
		super(line,col);
		label = i;
	} // breakNode

	private final identNode label;
} // class breakNode 

class continueNode extends stmtNode {
	continueNode(identNode i, int line, int col) {
		super(line,col);
		label = i;
	} // continueNode

	private final identNode label;
} // class continueNode 

class argsNode extends ASTNode {
	argsNode() {}
	argsNode(exprNode e, argsNode a, int line, int col) {
		super(line,col);
		argVal = e;
		moreArgs = a;
	} // argsNode

	static nullArgsNode NULL = new nullArgsNode();
	private exprNode argVal;
	private argsNode moreArgs;
} // class argsNode 

class nullArgsNode extends argsNode {
	nullArgsNode() {}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
} // class nullArgsNode 

class strLitNode extends exprNode {
	strLitNode(String stringval, int line, int col) {
		// complete this constructor
	} // strLitNode

	private String strval;
} // class strLitNode 

// abstract superclass; only subclasses are actually created
abstract class exprNode extends ASTNode {
	exprNode() {super();}
	exprNode(int l,int c) {
		super(l,c);
		type = new Types();
		kind = new Kinds();
	} // exprNode
	exprNode(int l,int c,Types t,Kinds k) {
		super(l,c);
		type = t;
		kind = k;
	} // exprNode
	static nullExprNode NULL = new nullExprNode();
		protected Types type; // Used for typechecking: the type of this node
		protected Kinds kind; // Used for typechecking: the kind of this node
} // abstract class exprNode

class nullExprNode extends exprNode {
	nullExprNode() {super();}
	boolean isNull() {return true;}
	void Unparse(int indent) {}
	void checkTypes() {} 
} // class nullExprNode 

class binaryOpNode extends exprNode {
	binaryOpNode(exprNode e1, int op, exprNode e2, int line, int col,
			Types resultType) {
		super(line,col, resultType, new Kinds(Kinds.Value));
		operatorCode = op;
		leftOperand = e1;
		rightOperand = e2;
	} // binaryOpNode

	static void printOp(int op) {
		switch (op) {
			case sym.PLUS:
				System.out.print(" + ");
				break;
			case sym.MINUS:
				System.out.print(" - ");
				break;
			default:
				mustBe(false);
		} // switch(op)
	} // printOp

	static String toString(int op) {
		switch (op) {
			case sym.PLUS:
				return(" + ");
			case sym.MINUS:
				return(" - ");
			default:
				mustBe(false);
				return "";
		} // switch(op)
	} // toString

	void Unparse(int indent) {
		System.out.print("(");
		leftOperand.Unparse(0);
		printOp(operatorCode);
		rightOperand.Unparse(0);
		System.out.print(")");
	} // Unparse

	void checkTypes() {
		mustBe(operatorCode== sym.PLUS
		||operatorCode==sym.MINUS);//Only two bin ops in CSX-lite
		leftOperand.checkTypes();
		rightOperand.checkTypes();
		type = new Types(Types.Integer);
		typeMustBe(leftOperand.type.val, Types.Integer,
			error() + "Left operand of" + toString(operatorCode) 
					+ "must be an int.");
		typeMustBe(rightOperand.type.val, Types.Integer,
			error() + "Right operand of" + toString(operatorCode) 
					+ "must be an int.");
	} // checkTypes

	private final exprNode leftOperand;
	private final exprNode rightOperand;
	private final int operatorCode; // Token code of the operator
} // class binaryOpNode 

class unaryOpNode extends exprNode {
	unaryOpNode(int op, exprNode e, int line, int col) {
		super(line,col);
		operand = e;
		operatorCode = op;
	} // unaryOpNode

	private exprNode operand;
	private int operatorCode; // Token code of the operator
} // class unaryOpNode 

class castNode extends exprNode {
	castNode(typeNode t, exprNode e, int line, int col) {
		super(line,col);
		operand = e;
		resultType = t;
	} // castNode

	private final exprNode operand;
	private final typeNode resultType;
} // class castNode 

class fctCallNode extends exprNode {
	fctCallNode(identNode id, argsNode a, int line, int col) {
		super(line,col);
		methodName = id;
		methodArgs = a;
	} // fctCallNode

	private final identNode methodName;
	private final argsNode methodArgs;
} // class fctCallNode 

class identNode extends exprNode {
	identNode(String identname, int line, int col) {
		super(line,col,new Types(Types.Unknown), new Kinds(Kinds.Var));
		idname = identname;
		nullFlag = false;
	} // identNode

	identNode(boolean flag) {
		super(0,0,new Types(Types.Unknown), new Kinds(Kinds.Var));
		idname = "";
		nullFlag = flag;
	} // identNode

	boolean isNull() {return nullFlag;} // Is this node null?

	static identNode NULL = new identNode(true);

	void Unparse(int indent) {
		System.out.print(idname);
	} // Unparse

	void checkTypes() {
		SymbolInfo id;
		mustBe(kind.val == Kinds.Var); //In CSX-lite all IDs should be vars! 
		id = (SymbolInfo) st.localLookup(idname);
		if (id == null) {
			System.out.println(error() + idname + " is not declared.");
			typeErrors++;
			type = new Types(Types.Error);
		} else {
			type = id.type; 
			idinfo = id; // Save ptr to correct symbol table entry
		} // id != null
	} // checkTypes

	public String idname;
	public SymbolInfo idinfo; // symbol table entry for this ident
	private final boolean nullFlag;
} // class identNode 

class intLitNode extends exprNode {
	intLitNode(int val, int line, int col) {
		super(line,col,new Types(Types.Integer),
				new Kinds(Kinds.Value));
		intval = val;
	} // intLitNode
	void Unparse(int indent) {
		System.out.print(intval);
	} // Unparse
	void checkTypes() {
		// All intLits are automatically type-correct
	} // checkTypes

	private final int intval;
} // class intLitNode 

class nameNode extends exprNode {
	nameNode(identNode id, exprNode expr, int line, int col) {
		super(line,col,new Types(Types.Unknown), new Kinds(Kinds.Var));
		varName = id;
		subscriptVal = expr;
	} // nameNode

	void Unparse(int indent) {
		varName.Unparse(0); // Subscripts not allowed in CSX Lite
	} // Unparse

	void checkTypes() {
		varName.checkTypes(); // Subscripts not allowed in CSX Lite
		type = varName.type;
	} // checkTypes

	private final identNode varName;
	private final exprNode subscriptVal;
} // class nameNode 

class charLitNode extends exprNode {
	charLitNode(char val, int line, int col) {
		super(line, col);
		charval = val;
	} // charLitNode

	private final char charval;
} // class charLitNode  // class charLitNode 

class trueNode extends exprNode {
	trueNode(int line, int col) {
		super(line, col);
	} // trueNode
} // class trueNode  // class trueNode 

class falseNode extends exprNode {
	falseNode(int line, int col) {
		super(line, col);
	} // falseNode
} // class falseNode

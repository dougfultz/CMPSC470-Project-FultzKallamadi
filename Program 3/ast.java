abstract class ASTNode {
// abstract superclass; only subclasses are actually created

	int linenum;
	int colnum;

	static void genIndent(int indent) {
		for (int i = 1; i <= indent; i++) {
			System.out.print("\t");
		}
	} // genIndent

	ASTNode() {
		linenum = -1;
		colnum = -1;
	} // ASTNode()

	ASTNode(int line, int col) {
		linenum = line;
		colnum = col;
	} // ASTNode(line, col)

	boolean isNull() {
		return false; // often redefined in a subclass
	} // isNull()

	void Unparse(int indent) {
		// This routine is normally redefined in a subclass
	} // Unparse()
} // class ASTNode

class nullNode extends ASTNode {
// This class definition probably doesn't need to be changed
	nullNode() {
		super();
	}

	boolean isNull() {
		return true;
	}

	void Unparse(int indent) {
		// no action
	}
} // class nullNode

class csxLiteNode extends ASTNode {
// This node is used to root CSX lite programs 
	
	csxLiteNode(stmtsNode stmts, int line, int col) {
		super(line, col);
		progStmts = stmts;
	} // csxLiteNode() 

	void Unparse(int indent) {
		System.out.println(linenum + ":" + " {");
		progStmts.Unparse(1);
		System.out.println(linenum + ":" + " } EOF");
	} // Unparse()

	private final stmtsNode progStmts;
} // class csxLiteNode

class classNode extends ASTNode {

	classNode(identNode id, memberDeclsNode memb, int line, int col) {
		super(line, col);
		className = id;
		members = memb;
	} // classNode
	
	void Unparse(int indent) {
		System.out.println(linenum + ":" + "class ");
		className.Unparse(0);
		System.out.println(" {");
		members.Unparse(1);
		System.out.println(linenum + ":" +"}");
	} // Unparse()

	private final identNode className;
	private final memberDeclsNode members;
} // class classNode

class memberDeclsNode extends ASTNode {
	memberDeclsNode(fieldDeclsNode f, methodDeclsNode m, int line, int col) {
		super(line, col);
		fields = f;
		methods = m;
	}

	void Unparse(int indent) {
		fields.Unparse(0);
		methods.Unparse(1);
	}
	fieldDeclsNode fields;
    //Make public
	methodDeclsNode methods;
} // class memberDeclsNode

class fieldDeclsNode extends ASTNode {
	fieldDeclsNode() {
		super();
	}
	fieldDeclsNode(declNode d, fieldDeclsNode f, int line, int col) {
		super(line, col);
		thisField = d;
		moreFields = f;
	}

	void Unparse(int indent){
		thisField.Unparse(0);
		moreFields.Unparse(1);
	}
	static nullFieldDeclsNode NULL = new nullFieldDeclsNode();
	private declNode thisField;
	private fieldDeclsNode moreFields;
} // class fieldDeclsNode

class nullFieldDeclsNode extends fieldDeclsNode {
	nullFieldDeclsNode() {}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {
	
	}
} // class nullFieldDeclsNode

// abstract superclass; only subclasses are actually created
abstract class declNode extends ASTNode {
	declNode() {
		super();
	}
	declNode(int l, int c) {
		super(l, c);
	}
} // class declNode

class varDeclNode extends declNode {
	varDeclNode(identNode id, typeNode t, exprNode e, int line, int col) {
		super(line, col);
		varName = id;
		varType = t;
		initValue = e;
	}

	void Unparse(int indent){ 
		System.out.println(linenum + ":\t");
		genIndent(indent);
		varType.Unparse(0);
		System.out.print(" ");
		varName.Unparse(1);
	
	
		if(!initValue.isNull()){
			System.out.println(" = ");
			initValue.Unparse(2);
		}
	}
	private final identNode varName;
	private final typeNode varType;
	private final exprNode initValue;
} // class varDeclNode

class constDeclNode extends declNode {
	constDeclNode(identNode id,  exprNode e, int line, int col) {
		super(line, col);
		constName = id;
		constValue = e;
	}

	void Unparse(int indent){
		System.out.print(linenum + ":\t");
		genIndent(indent);
		System.out.print("const ");
		constName.Unparse(0);
		System.out.print(" = ");
		constValue.Unparse(1);
		System.out.print(";");

	
	}

	private final identNode constName;
	private final exprNode constValue;
} // class constDeclNode

class arrayDeclNode extends declNode {
	arrayDeclNode(identNode id, typeNode t, intLitNode lit, int line, int col) {
		super(line, col);
		arrayName = id;
		elementType = t;
		arraySize = lit;
	}

	void Unparse(int indent){
		System.out.print(linenum + ":\t");
		genIndent(indent);
		arrayName.Unparse(0);
		elementType.Unparse(1);
		System.out.print("[");
		arraySize.Unparse(2);
		System.out.println("]");
	}
	
	private final identNode arrayName;
	private final typeNode elementType;
	private final intLitNode arraySize;
} // class arrayDeclNode

abstract class typeNode extends ASTNode {
// abstract superclass; only subclasses are actually created
	typeNode() {
		super();
	}
	typeNode(int l, int c) {
		super(l, c);
	}

	void Unparse(int indent){

	}
	static nullTypeNode NULL = new nullTypeNode();
} // class typeNode

class nullTypeNode extends typeNode {
	nullTypeNode() {}
	boolean isNull() {
		return true;
	}
	void Unparse(int indent) {}
} // class nullTypeNode

class intTypeNode extends typeNode {
	intTypeNode(int line, int col) {
		super(line, col);
	}
	
	void Unparse(int indent){
		System.out.print("int ");
	}
} // class intTypeNode

class floatTypeNode extends typeNode {
	floatTypeNode(int line, int col) {
		super(line, col);
	}

	void Unparse(int indent){
		System.out.print("float ");
	}
} // class floatTypeNode

class boolTypeNode extends typeNode {
	boolTypeNode(int line, int col) {
		super(line, col);
	}

	void Unparse(int indent){
		System.out.print("bool ");
	}
} // class boolTypeNode

class charTypeNode extends typeNode {
	charTypeNode(int line, int col) {
		super(line, col);
	}

	void Unparse(int indent){
		System.out.print("char ");
	}

} // class charTypeNode

class voidTypeNode extends typeNode {
	voidTypeNode(int line, int col) {
		super(line, col);
	}

	void Unparse(int indent){
		System.out.print("void");
	}
} // class voidTypeNode

class methodDeclsNode extends ASTNode {
	methodDeclsNode() {
		super();
	}
	methodDeclsNode(methodDeclNode m, methodDeclsNode ms,
			int line, int col) {
		super(line, col);
		thisDecl = m;
	 moreDecls = ms;
	}

	void Unparse(int indent){
		thisDecl.Unparse(0);
		moreDecls.Unparse(1);
	}
	static nullMethodDeclsNode NULL = new nullMethodDeclsNode();
	private methodDeclNode thisDecl;
	private methodDeclsNode moreDecls;
} // class methodDeclsNode 

class nullMethodDeclsNode extends methodDeclsNode {
	nullMethodDeclsNode() {}
	boolean   isNull() {return true;}
	
	void Unparse(int indent) {
	
	}
} // class nullMethodDeclsNode 

class methodDeclNode extends ASTNode {
	methodDeclNode(identNode id, argDeclsNode a, typeNode t,
			fieldDeclsNode f, stmtsNode s, int line, int col) {
		super(line, col);
		name = id;
		args = a;
		returnType = t;
		decls = f;
		stmts = s;
	}

	private final identNode name;
	private final argDeclsNode args;
	private final typeNode returnType;
	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class methodDeclNode 

// abstract superclass; only subclasses are actually created
abstract class argDeclNode extends ASTNode {
	argDeclNode() {
		super();
	}
	argDeclNode(int l, int c) {
		super(l, c);
	}
}

class argDeclsNode extends ASTNode {
	argDeclsNode() {}
	argDeclsNode(argDeclNode arg, argDeclsNode args,
			int line, int col) {
		super(line, col);
		thisDecl = arg;
		moreDecls = args;
	}

	void Unparse(int indent){
		thisDecl.Unparse(0);
		if(!moreDecls.isNull()){
			System.out.print(", ");
		}
		moreDecls.Unparse(1);
	}

	static nullArgDeclsNode NULL = new nullArgDeclsNode();

	private argDeclNode thisDecl;
	private argDeclsNode moreDecls;
} // class argDeclsNode 

class nullArgDeclsNode extends argDeclsNode {
	nullArgDeclsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullArgDeclsNode 

class arrayArgDeclNode extends argDeclNode {
	arrayArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line, col);
		argName = id;
		elementType = t;
	}

	void Unparse(int indent){
		argName.Unparse(0);
		elementType.Unparse(1);
		System.out.print("[]");
	}

	private final identNode argName;
	private final typeNode elementType;
} // class arrayArgDeclNode 

class valArgDeclNode extends argDeclNode {
	valArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line, col);
		argName = id;
		argType = t;
	}

	void Unparse(int indent){
		argName.Unparse(0);
		argType.Unparse(1);	
	}

	private final identNode argName;
	private final typeNode argType;
} // class valArgDeclNode 

// abstract superclass; only subclasses are actually created
abstract class stmtNode extends ASTNode {
	stmtNode() {
		super();
	}
	stmtNode(int l, int c) {
		super(l, c);
	}
	static nullStmtNode NULL = new nullStmtNode();
}

class nullStmtNode extends stmtNode {
	nullStmtNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullStmtNode 

class stmtsNode extends ASTNode {
	stmtsNode(stmtNode stmt, stmtsNode stmts, int line, int col) {
		super(line, col);
		thisStmt = stmt;
		moreStmts = stmts;
	}
	stmtsNode() {}

	void Unparse(int indent) {
		thisStmt.Unparse(indent);
		moreStmts.Unparse(indent);
	} 

	static nullStmtsNode NULL = new nullStmtsNode();
	private stmtNode thisStmt;
	private stmtsNode moreStmts;
} // class stmtsNode 

class nullStmtsNode extends stmtsNode {
	nullStmtsNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}

} // class nullStmtsNode 

class asgNode extends stmtNode {
	asgNode(nameNode n, exprNode e, int line, int col) {
		super(line, col);
		target = n;
		source = e;
	}

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		target.Unparse(0);
		System.out.print(" = ");
		source.Unparse(0);
		System.out.println(";");
	}

	private final nameNode target;
	private final exprNode source;
} // class asgNode 

class ifThenNode extends stmtNode {
	ifThenNode(exprNode e, stmtNode s1, stmtNode s2, int line, int col) {
		super(line, col);
		condition = e;
		thenPart = s1;
		elsePart = s2;
	}

	void Unparse(int indent) {
		System.out.print(linenum + ":");
		genIndent(indent);
		System.out.print("if (");
		condition.Unparse(0);
		System.out.println(")");
		thenPart.Unparse(indent+1);
		// No else parts in CSX-lite
	}

	private final exprNode condition;
	private final stmtNode thenPart;
	private final stmtNode elsePart;
} // class ifThenNode 

class whileNode extends stmtNode {
	whileNode(exprNode i, exprNode e, stmtNode s, int line, int col) {
		super(line, col);
	 label = i;
	 condition = e;
	 loopBody = s;
	}

	private final exprNode label;
	private final exprNode condition;
	private final stmtNode loopBody;
} // class whileNode 

class readNode extends stmtNode {
	readNode() {}
	readNode(nameNode n, readNode rn, int line, int col) {
		super(line, col);
		 targetVar = n;
		 moreReads = rn;
	}

	void Unparse(int indent){
		if(targetVar.linenum==-1){
			System.out.print(linenum + ":\t");
			genIndent(indent);	
			System.out.print("read(");
				if(!moreReads.isNull()){
					moreReads.Unparse(0);
				}
			}
			else {
				targetVar.Unparse(1);
				if(!moreReads.isNull()) {
					System.out.print(", ");
					moreReads.Unparse(1); 
				}
				else {
					System.out.print(")\n");
				}
			}
	}
	static nullReadNode NULL = new nullReadNode();
	private nameNode targetVar;
	private readNode moreReads;
} // class readNode 

class nullReadNode extends readNode {
	nullReadNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullReadNode 

class printNode extends stmtNode {
	printNode() {}
	printNode(exprNode val, printNode pn, int line, int col) {
		super(line, col);
		outputValue = val;
		morePrints = pn;
	}
	static nullPrintNode NULL = new nullPrintNode();

	private exprNode outputValue;
	private printNode morePrints;
} // class printNode 

class nullPrintNode extends printNode {
	nullPrintNode() {}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullprintNode

class callNode extends stmtNode {
	callNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		args = a;
	}

	private final identNode methodName;
	private final argsNode args;
} // class callNode 

class returnNode extends stmtNode {
	returnNode(exprNode e, int line, int col) {
		super(line, col);
		returnVal = e;
	}

	private final exprNode returnVal;
} // class returnNode 

class blockNode extends stmtNode {
	blockNode(fieldDeclsNode f, stmtsNode s, int line, int col) {
		super(line, col);
		decls = f;
		stmts = s;
	}

	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class blockNode 

class breakNode extends stmtNode {
	breakNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	}

	private final identNode label;
} // class breakNode 

class continueNode extends stmtNode {
	continueNode(identNode i, int line, int col) {
		super(line, col);
		label = i;
	}

	private final identNode label;
} // class continueNode 

class argsNode extends ASTNode {
	argsNode() {}
	argsNode(exprNode e, argsNode a, int line, int col) {
		super(line, col);
		argVal = e;
		moreArgs = a;
	}

	static nullArgsNode NULL = new nullArgsNode();
	private exprNode argVal;
	private argsNode moreArgs;
} // class argsNode 

class nullArgsNode extends argsNode {
	nullArgsNode() {
		// empty constructor
	}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullArgsNode 

class strLitNode extends exprNode {
	strLitNode(String stringval, int line, int col) {
		super(line, col);
		strval = stringval;
	}

	private final String strval;
} // class strLitNode 

// abstract superclass; only subclasses are actually created
abstract class exprNode extends ASTNode {
	exprNode() {
		super();
	}
	exprNode(int l, int c) {
		super(l, c);
	}
	static nullExprNode NULL = new nullExprNode();
}

class nullExprNode extends exprNode {
	nullExprNode() {
		super();
	}
	boolean   isNull() {return true;}
	void Unparse(int indent) {}
} // class nullExprNode 

class binaryOpNode extends exprNode {
	binaryOpNode(exprNode e1, int op, exprNode e2, int line, int col) {
		super(line, col);
		operatorCode = op;
		leftOperand = e1;
		rightOperand = e2;
	}

	static void printOp(int op) {
		switch (op) {
			case sym.PLUS:
				System.out.print(" + ");
				break;
			case sym.MINUS:
				System.out.print(" - ");
				break;
			case sym.SLASH:
				System.out.print(" / ");
				break;
			case sym.TIMES:
				System.out.print(" * ");
				break;

			case sym.EQ:
				System.out.print(" = ");
				break;
			case sym.NOTEQ:
				System.out.print(" != ");
				break;
			case sym.GEQ:
				System.out.print(" >= ");
				break;
			case sym.GT:
				System.out.print(" > ");
				break;
			case sym.LEQ:
				System.out.print(" <= ");
				break;
			case sym.LT:
				System.out.print(" < " );
				break;
			case sym.CAND:
				System.out.print(" && ");
				break;
			case sym.COR:
				System.out.print(" || ");
				break;
			case sym.NOT:
				System.out.print(" ! " );
				break;				

			default:
				throw new Error("printOp: case not found");
		}
	}

	void Unparse(int indent) {
		System.out.print("(");
		leftOperand.Unparse(0);
		printOp(operatorCode);
		rightOperand.Unparse(0);
		System.out.print(")");
	}

	private final exprNode leftOperand;
	private final exprNode rightOperand;
	private final int operatorCode; // Token code of the operator
} // class binaryOpNode 

class unaryOpNode extends exprNode {
	unaryOpNode(int op, exprNode e, int line, int col) {
		super(line, col);
		operand = e;
		operatorCode = op;
	}

	private final exprNode operand;
	private final int operatorCode; // Token code of the operator
} // class unaryOpNode 

class castNode extends exprNode {
	castNode(typeNode t, exprNode e, int line, int col) {
		super(line, col);
		operand = e;
		resultType = t;
	}

	private final exprNode operand;
	private final typeNode resultType;
} // class castNode 

class fctCallNode extends exprNode {
	fctCallNode(identNode id, argsNode a, int line, int col) {
		super(line, col);
		methodName = id;
		methodArgs = a;
	}

	private final identNode methodName;
	private final argsNode methodArgs;
} // class fctCallNode 

class identNode extends exprNode {
	identNode(String identname, int line, int col) {
		super(line, col);
		idname   = identname;
	}

	void Unparse(int indent) {
		System.out.print(idname);
	}

	private final String idname;
} // class identNode 

class nameNode extends exprNode {
	nameNode(identNode id, exprNode expr, int line, int col) {
		super(line, col);
		varName = id;
		subscriptVal = expr;
	}

	void Unparse(int indent) {
		varName.Unparse(0); // Subscripts not allowed in CSX Lite
	}

	private final identNode varName;
	private final exprNode subscriptVal;
} // class nameNode 

class intLitNode extends exprNode {
	intLitNode(int val, int line, int col) {
		super(line, col);
		intval = val;
	}

	private final int intval;
} // class intLitNode

class floatLitNode extends exprNode {
	floatLitNode(float val, int line, int col) {
		super(line, col);
		floatval = val;
	}

	private final float floatval;
} // class floatLitNode

class charLitNode extends exprNode {
	charLitNode(char val, int line, int col) {
		super(line, col);
		charval = val;
	}

	private final char charval;
} // class charLitNode 

class trueNode extends exprNode {
	trueNode(int line, int col) {
		super(line, col);
	}
} // class trueNode 

class falseNode extends exprNode {
	falseNode(int line, int col) {
		super(line, col);
	}
} // class falseNode 

class incNode extends stmtNode {
    incNode(nameNode n, int line, int col) {
        super(line, col);
        target = n;
    }
    
    void Unparse(int indent) {
        //TODO
    }
    
    private final nameNode target;
} // class incNode

class decNode extends stmtNode {
    decNode(nameNode n, int line, int col) {
        super(line, col);
        target = n;
    }
    
    void Unparse(int indent) {
        //TODO
    }
    
    private final nameNode target;
} // class decNode

class forNode extends stmtNode {
    forNode(stmtNode i, exprNode c, stmtNode u, stmtNode b, int line, int col) {
        super(line, col);
        init = i;
        condition = c;
        update = u;
        loopBody = b;
    }
    
    private final stmtNode init;
    private final exprNode condition;
    private final stmtNode update;
    private final stmtNode loopBody;
} // class forNode

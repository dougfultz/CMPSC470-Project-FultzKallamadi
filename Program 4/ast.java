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
    void checkTypes() {
        //Add className to scope
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(className.idname);
        if (id == null) {
            id = new SymbolInfo(className.idname,
                new Kinds(Kinds.Other),new Types(Types.Unknown));
            className.type = new Types(Types.Unknown);
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            className.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            className.type = new Types(Types.Error);
        } // id != null
        
        //check types
        className.checkTypes();
        members.checkTypes();
    } // checkTypes
    boolean isTypeCorrect() {
        //Pulled from csxLiteNode.isTypeCorrect()
        st.openScope();
        checkTypes();
        return (typeErrors == 0);
    }
	private final identNode className;
	private final memberDeclsNode members;
} // class classNode

class memberDeclsNode extends ASTNode {
	memberDeclsNode(fieldDeclsNode f, methodDeclsNode m, int line, int col) {
		super(line,col);
		fields = f;
		methods = m;
	} // memberDeclsNode
    void checkTypes() {
        fields.checkTypes();
        methods.checkTypes();
    } // checkTypes
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
	constDeclNode(identNode id, typeNode t, exprNode e, int line, int col) {
		super(line,col);
		constName = id;
        constType = t;
		constValue = e;
	} // constDeclNode
    
    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(constName.idname);
        if (id == null) {
            id = new SymbolInfo(constName.idname,
                new Kinds(Kinds.Var),constType.type);
            constName.type = constType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            constName.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            constName.type = new Types(Types.Error);
        } // id != null
    } // checkTypes

	private final identNode constName;
    private final typeNode constType;
	private final exprNode constValue;
}


class arrayDeclNode extends declNode {
	arrayDeclNode(identNode id, typeNode t, intLitNode lit, int line, int col) {
		super(line,col);
		arrayName = id;
		elementType = t;
		arraySize = lit;
	} // arrayDeclNode

    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(arrayName.idname);
        if (id == null) {
            id = new SymbolInfo(arrayName.idname,
                new Kinds(Kinds.Var),elementType.type);
            arrayName.type = elementType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            arrayName.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            arrayName.type = new Types(Types.Error);
        } // id != null
        
        //Size of array must be greater than zero
        if(arraySize.getVal()<=0){
            typeMustBe(0,1,error() + arraySize.getVal() + ": size of array must be greater than zero");
        }
    } // checkTypes

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
    
    void checkTypes() {
        thisDecl.checkTypes();
        moreDecls.checkTypes();
    } // checkTypes
    
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
    
    void checkTypes() {
        //add method declaration to scope
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(name.idname);
        if (id == null) {
            id = new SymbolInfo(name.idname,
                new Kinds(Kinds.Method),returnType.type);
            name.type = returnType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            name.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            name.type = new Types(Types.Error);
        } // id != null
        
        //Open scope
        st.openScope();
        //Type check everything else
        args.checkTypes();
        decls.checkTypes();
        stmts.checkTypes();
        //Close scope
        try{
            st.closeScope();
        }catch(EmptySTException e){
            System.err.println("Empty scope");
            System.exit(-1);
        }
    } // checkTypes

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
    
    void checkTypes() {
        thisDecl.checkTypes();
        moreDecls.checkTypes();
    } // checkTypes

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
    
    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(argName.idname);
        if (id == null) {
            id = new SymbolInfo(argName.idname,
                new Kinds(Kinds.Var),elementType.type);
            argName.type = elementType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            argName.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            argName.type = new Types(Types.Error);
        } // id != null
    } // checkTypes

	private final identNode argName;
	private final typeNode elementType;
} // class arrayArgDeclNode 

class valArgDeclNode extends argDeclNode {
	valArgDeclNode(identNode id, typeNode t, int line, int col) {
		super(line,col);
		argName = id;
		argType = t;
	} // valArgDeclNode
    
    void checkTypes() {
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(argName.idname);
        if (id == null) {
            id = new SymbolInfo(argName.idname,
                new Kinds(Kinds.Var),argType.type);
            argName.type = argType.type;
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            argName.idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            argName.type = new Types(Types.Error);
        } // id != null
    } // checkTypes

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
    
    void checkTypes() {
        //Add label to scope
        SymbolInfo id;
        id = (SymbolInfo) st.localLookup(((identNode)label).idname);
        if (id == null) {
            id = new SymbolInfo(((identNode)label).idname,
                new Kinds(Kinds.Var),new Types(Types.Void));
            label.type = new Types(Types.Void);
            try {
                st.insert(id);
            } catch (DuplicateException d) {
                /* can't happen */
            } catch (EmptySTException e) {
                /* can't happen */
            }
            ((identNode)label).idinfo = id;
        } else {
            System.out.println(error() + id.name() + " is already declared.");
            typeErrors++;
            label.type = new Types(Types.Error);
        } // id != null
        
        //type check condition
        condition.checkTypes();
        //type check loop body
        loopBody.checkTypes();
    } // checkTypes

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
    
    void checkTypes() {
        //Have current node type check itself
        targetVar.checkTypes();
        
        //Check kind of current variable
        switch(targetVar.kind.val){
            case Kinds.Value:
            case Kinds.Var:
                break;
            default:
                //Show an error
                typeMustBe(0,1,error() + targetVar.kind.toString() + ": Valid kinds for read are: value & var");
        } //switch(targetVar.kind.val)
        
        //Check type of current variable
        switch(targetVar.type.val){
            case Types.Integer:
            case Types.Character:
                break;
            default:
                //Show an error
                typeMustBe(0,1,error() + targetVar.type.toString() + ": Valid types for read are: int or char");
        } // switch(targetVar.type.val)
        
        //Check remaining nodes
        moreReads.checkTypes();
    } // checkTypes

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
        //Have current node type check itself
        outputValue.checkTypes();
        
        //Check kind of current variable
        switch(outputValue.kind.val){
            case Kinds.Value:
            case Kinds.Var:
            case Kinds.Array:
                break;
            default:
                //Show an error
                typeMustBe(0,1,error() + outputValue.kind.toString() + ": Valid kinds for print are: value, var, or array");
        } //switch(outputValue.kind.val)
        
        //Check type of current variable
        switch(outputValue.type.val){
            case Types.Integer:
            case Types.Boolean:
            case Types.Real:
            case Types.Character:
            case Types.String:
                break;
            default:
                //Show an error
                typeMustBe(0,1,error() + outputValue.type.toString() + ": Valid types for print are: int, bool, float, char, or string");
        } //switch(outputValue.type.val)
        
        //Check remaining nodes
        morePrints.checkTypes();
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
    
    void checkTypes() {
        methodName.checkTypes();
        args.checkTypes();
    } // checkTypes

	private final identNode methodName;
	private final argsNode args;
} // class callNode 

class returnNode extends stmtNode {
	returnNode(exprNode e, int line, int col) {
		super(line,col);
		returnVal = e;
	} // returnNode
    
    void checkTypes() {
        returnVal.checkTypes();
    } // checkTypes

	private final exprNode returnVal;
} // class returnNode 

class blockNode extends stmtNode {
	blockNode(fieldDeclsNode f, stmtsNode s, int line, int col) {
		super(line,col);
		decls = f;
		stmts = s;
	} // blockNode
    
    void checkTypes() {
        //Open scope
        st.openScope();
        //Type check everything else
        decls.checkTypes();
        stmts.checkTypes();
        //Close scope
        try{
            st.closeScope();
        }catch(EmptySTException e){
            System.err.println("Empty scope");
            System.exit(-1);
        }
    } // checkTypes

	private final fieldDeclsNode decls;
	private final stmtsNode stmts;
} // class blockNode 

class breakNode extends stmtNode {
	breakNode(identNode i, int line, int col) {
		super(line,col);
		label = i;
	} // breakNode
    
    void checkTypes() {
        label.checkTypes();
    } // checkTypes

	private final identNode label;
} // class breakNode 

class continueNode extends stmtNode {
	continueNode(identNode i, int line, int col) {
		super(line,col);
		label = i;
	} // continueNode
    
    void checkTypes() {
        label.checkTypes();
    } // checkTypes

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
        // complete this constructor - DONE
        super(line,col);
        strval = stringval;
	} // strLitNode
    
    void checkTypes() {
        // All strLits are automatically type-correct
    } // checkTypes

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
        System.out.print(toString(op));
	} // printOp

	static String toString(int op) {
		switch (op) {
			case sym.PLUS:
				return(" + ");
			case sym.MINUS:
				return(" - ");
            case sym.SLASH:
                return(" / ");
            case sym.TIMES:
                return(" * ");
            case sym.EQ:
                return(" == ");
            case sym.NOTEQ:
                return(" != ");
            case sym.GEQ:
                return(" >= ");
            case sym.GT:
                return(" > ");
            case sym.LEQ:
                return(" <= ");
            case sym.LT:
                return(" < " );
            case sym.CAND:
                return(" && ");
            case sym.COR:
                return(" || ");
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
        //Check types of operands
        leftOperand.checkTypes();
        rightOperand.checkTypes();
        
        //Do something based on operator
        switch(operatorCode){
            case sym.PLUS:
            case sym.MINUS:
            case sym.SLASH:
            case sym.TIMES:
                //Check if left operand type is valid for this operator
                switch(leftOperand.type.val){
                    case Types.Integer:
                    case Types.Character:
                        //check the right operand type
                        switch(rightOperand.type.val){
                            case Types.Integer:
                            case Types.Character:
                                break;
                            default:
                                //Show an error
                                typeMustBe(0,1,error() + "Valid types of right operand are: int or char");
                        } //switch(rightOperand.type.val)
                        //Set Type to the left operand type
                        type=leftOperand.type;
                        break;
                    case Types.Real:
                        //check the right operand type
                        switch(rightOperand.type.val){
                            case Types.Real:
                                break;
                            default:
                                //Show an error
                                typeMustBe(0,1,error() + "Valid type of right operand is: float");
                        } //switch(rightOperand.type.val)
                        //Set Type to the left operand type
                        type=leftOperand.type;
                        break;
                    default:
                        //Show an error
                        typeMustBe(0,1,error() + "Valid types of left operand are: int, float, or char");
                } //switch(leftOperand.type.val)
                break;
            case sym.EQ:
            case sym.NOTEQ:
            case sym.GEQ:
            case sym.GT:
            case sym.LEQ:
            case sym.LT:
                //Check if types of operands match
                typeMustBe(leftOperand.type.val, rightOperand.type.val,
                    error() + leftOperand.type.toString() + toString(operatorCode) + rightOperand.type.toString()
                            + ": Left operand and right operand must be of same type");
                //Check if types of operands are valid for comparison
                switch(leftOperand.type.val){
                    case Types.Integer:
                    case Types.Real:
                    case Types.Character:
                    case Types.Boolean:
                        break;
                    default:
                        //Show an error
                        typeMustBe(0,1,error() + "Valid types are: int, float, char, or bool");
                } //switch(leftOperand.type.val)
                //Set Type
                type=new Types(Types.Boolean);
                break;
            case sym.CAND:
            case sym.COR:
                //Check if types of operands match
                typeMustBe(leftOperand.type.val, rightOperand.type.val,
                    error() + leftOperand.type.toString() + toString(operatorCode) + rightOperand.type.toString()
                            + "\nLeft operand and right operand must be of same type");
                //Check if types of operands are valid for comparison
                switch(leftOperand.type.val){
                    case Types.Boolean:
                        break;
                    default:
                        //Show an error
                        typeMustBe(0,1,error() + "Valid type is: bool");
                } //switch(leftOperand.type.val)
                //Set Type
                type=new Types(Types.Boolean);
                break;
            default:
                mustBe(false);
        } // switch(operatorCode)
        
        //Binary Ops are always a value
        kind=new Kinds(Kinds.Value);
        
	} // checkTypes

	private final exprNode leftOperand;
	private final exprNode rightOperand;
	private final int operatorCode; // Token code of the operator
} // class binaryOpNode 

class unaryOpNode extends exprNode {
	unaryOpNode(int op, exprNode e, int line, int col, Types resultType) {
		super(line,col, resultType, new Kinds(Kinds.Value));
		operand = e;
		operatorCode = op;
	} // unaryOpNode
    
    static void printOp(int op) {
        System.out.print(toString(op));
    } // printOp
    
    static String toString(int op) {
        switch (op) {
            case sym.NOT:
                return(" ! ");
            default:
                mustBe(false);
                return "";
        } // switch(op)
    } // toString
    
    void checkTypes() {
        //Check the type of the operand
        operand.checkTypes();
        
        //Do something based on operator
        switch(operatorCode){
            case sym.NOT:
                typeMustBe(operand.type.val,Types.Boolean,error() + ": operand must be a bool");
                //Set Type
                type=new Types(Types.Boolean);
                break;
            default:
                mustBe(false);
        } // switch(operatorCode)
        
        //Unary Ops are always a value
        kind=new Kinds(Kinds.Value);
    } // checkTypes

	private exprNode operand;
	private int operatorCode; // Token code of the operator
} // class unaryOpNode 

class castNode extends exprNode {
	castNode(typeNode t, exprNode e, int line, int col) {
		super(line,col);
		operand = e;
		resultType = t;
	} // castNode
    
    void checkTypes() {
        //Type check children
        operand.checkTypes();
        resultType.checkTypes();
        
        //Check if operand is a valid type for casting
        switch(operand.type.val){
            case Types.Integer:
            case Types.Character:
            case Types.Boolean:
                //Check if result type is valid
                switch(resultType.type.val){
                    case Types.Integer:
                    case Types.Character:
                    case Types.Real:
                    case Types.Boolean:
                        break;
                    default:
                        //Show an error
                        typeMustBe(0,1,error() + resultType.type.toString() + ": Valid types for casting are: int, char, float, or bool");
                } //switch(resultType.type.val)
                break;
            default:
                //Show an error
                typeMustBe(0,1,error() + operand.type.toString() + ": Valid types of operand are: int, char, or bool");
        } //switch(operand.type.val)
        
        //Set type to result type
        type = resultType.type;
    } // checkTypes

	private final exprNode operand;
	private final typeNode resultType;
} // class castNode 

class fctCallNode extends exprNode {
	fctCallNode(identNode id, argsNode a, int line, int col) {
		super(line,col);
		methodName = id;
		methodArgs = a;
	} // fctCallNode
    
    void checkTypes() {
        methodName.checkTypes();
        methodArgs.checkTypes();
    } // checkTypes

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
		id = (SymbolInfo) st.globalLookup(idname);
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
    
    int getVal(){
        return(intval);
    }

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
    
    void checkTypes() {
        // All charLits are automatically type-correct
    } // checkTypes

	private final char charval;
} // class charLitNode  // class charLitNode 

class trueNode extends exprNode {
	trueNode(int line, int col) {
		super(line, col);
        type=new Types(Types.Boolean);
        kind=new Kinds(Kinds.Value);
	} // trueNode
} // class trueNode  // class trueNode 

class falseNode extends exprNode {
	falseNode(int line, int col) {
		super(line, col);
        type=new Types(Types.Boolean);
        kind=new Kinds(Kinds.Value);
	} // falseNode
} // class falseNode

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

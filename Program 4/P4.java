import java.io.*;
import java_cup.runtime.*;

class P4 {

	public static void
	main(String args[]) throws java.io.IOException,  Exception {

		if (args.length != 1) {
			System.out.println("Error: Input file must be named on command line." );
			System.exit(-1);
		}

		java.io.FileInputStream yyin = null;
		try {
			yyin = new java.io.FileInputStream(args[0]);
		} catch (FileNotFoundException notFound) {
			System.out.println ("Error: unable to open input file.");
			System.exit(-1);
		}

		Scanner.init(yyin); // Initialize Scanner class for parser
		final parser csxParser = new parser();
		System.out.println ("\n\n" + "Begin CSX compilation of " + args[0] + ".\n");
		Symbol root=null;
		try {
			root = csxParser.parse(); // do the parse
			System.out.println ("CSX program parsed correctly.");
		} catch (SyntaxErrorException e) {
			System.out.println ("Compilation terminated due to syntax errors.");
			System.exit(0);
		}

		final boolean ok = ((csxLiteNode)root.value).isTypeCorrect();
		if (ok) {
			System.out.println("No CSX type errors detected.");
		} else {
			System.out.println("\nCSX compilation halted due to type errors.");
		}
	} // main
} // class P4

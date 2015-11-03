import java.io.*;
import java_cup.runtime.*;

class P3 {

	public static void main(String args[]) throws java.io.IOException {
		if (args.length != 1) {
			System.out.println(
				"Error: Input file must be named on command line." );
			System.exit(-1);
		}
		java.io.FileInputStream yyin = null;
		try {
			yyin = new java.io.FileInputStream(args[0]);
		} catch (FileNotFoundException notFound){
			System.out.println ("Error: unable to open input file.");
			System.exit(-1);
		}
		Scanner.init(yyin); // Initialize Scanner class for parser
		final parser csxParser = new parser();
		Symbol root = null;
		try {
			// root = csxParser.debug_parse(); // do the parse
			root = csxParser.parse(); // do the parse
			System.out.println ("CSX program parsed correctly.");
		} catch (Exception e){
			System.out.println ("Compilation terminated due to syntax errors.");
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println ("Here is its unparsing:");
		((csxLiteNode)root.value).Unparse(0);
	} // main

} // class P3

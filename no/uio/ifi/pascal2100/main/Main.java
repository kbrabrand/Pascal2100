package no.uio.ifi.pascal2100.main;

import no.uio.ifi.pascal2100.parser.*;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

import java.io.*;

public class Main {
    public static final String version = "2015-08-18";

    //Del 3: public static Library library;
    public static LogFile log = new LogFile();

    private static String sourceFileName, baseFileName;
    private static boolean testParser = false, testScanner = false;

    public static void main(String arg[]) {
	System.out.println("This is the Ifi Pascal2100 compiler (" +
			   version + ")");

	int exitStatus = 0;
	try {
	    readArgs(arg);
	    log.init(baseFileName + ".log");

	    Scanner s = new Scanner(sourceFileName);
	    if (testScanner) 
		doTestScanner(s);
	    //Del 2: else if (testParser)
	    //    doTestParser(s);
	    // else
	    //    doRunRealCompiler(s);
	} catch (PascalError e) {
	    System.out.println();
	    System.err.println(e.getMessage());
	    exitStatus = 1;
	} finally {
	    log.finish();
	}

	System.exit(exitStatus);
    }


    private static void readArgs(String arg[]) {
	for (int i = 0;  i < arg.length;  i++) {
	    String a = arg[i];

	    if (a.equals("-logB")) {
		log.doLogBinding = true;
	    } else if (a.equals("-logP")) {
		log.doLogParser = true;
	    } else if (a.equals("-logS")) {
		log.doLogScanner = true;
	    } else if (a.equals("-logT")) {
		log.doLogTypeChecks = true;
	    } else if (a.equals("-logY")) {
		log.doLogPrettyPrint = true;
	    } else if (a.equals("-testparser")) {
		testParser = log.doLogParser = log.doLogPrettyPrint = true; 
	    } else if (a.equals("-testscanner")) {
		testScanner = log.doLogScanner = true; 
	    } else if (a.startsWith("-")) {
		warning("Warning: Unknown option " + a + " ignored.");
	    } else if (sourceFileName != null) {
		usage();
	    } else {
		sourceFileName = a;
	    }
	}
	if (sourceFileName == null) usage();
	
	baseFileName = sourceFileName;
	if (baseFileName.length()>4 && baseFileName.endsWith(".pas"))
	    baseFileName = baseFileName.substring(0,baseFileName.length()-4);
    }


    private static void doTestScanner(Scanner s) {
	while (s.nextToken.kind != eofToken)
	    s.readNextToken();
    }


    /* Del 2:
    private static void doTestParser(Scanner s) {
	Program prog = Program.parse(s);
	if (s.curToken.kind != eofToken) 
	    error("Scanner error: Garbage after the program!");

	prog.prettyPrint();
    }
    */

    /* Del 3 og 4:
    private static void doRunRealCompiler(Scanner s) {
	System.out.print("Parsing...");
	Program prog = Program.parse(s);
	if (s.curToken.kind != eofToken) 
	    error("Scanner error: Garbage after the program!");

	if (log.doLogPrettyPrint)
	    prog.prettyPrint();
	
	System.out.print(" checking...");
	library = new Library();
	prog.check(library, library);

	System.out.print(" generating code...");
	CodeFile code = new CodeFile(baseFileName+".s");
	library.genCode(code);  prog.genCode(code);
	code.finish();
	System.out.println("OK");

	assembleCode();
    }
    */


    private static void assembleCode() {
	String pName = baseFileName;
	String sName = baseFileName + ".s";

	String cmd[] = new String[8];
	cmd[0] = "gcc";  cmd[1] = "-m32";
	cmd[2] = "-o";   cmd[3] = pName;
	cmd[4] = sName;  
	cmd[5] = "-L.";  cmd[6] = "-L/hom/inf2100";  cmd[7] = "-lpas2100";  

	System.out.print("Running");
	for (String s: cmd) {
	    if (s.contains(" "))
		System.out.print(" '" + s + "'");
	    else
		System.out.print(" " + s);
	}
	System.out.println();

	try {
	    String line;
	    Process p = Runtime.getRuntime().exec(cmd);
	    BufferedReader out = new BufferedReader
		(new InputStreamReader(p.getInputStream()));
	    BufferedReader err = new BufferedReader
		(new InputStreamReader(p.getErrorStream()));

	    while ((line = out.readLine()) != null) {
		System.out.println(line);
	    }
	    while ((line = err.readLine()) != null) {
		System.out.println(line);
	    }
	    out.close();  err.close();
	    p.waitFor();
	} catch (Exception err) {
	    error("Assembly errors detected.");
	}
    }


    // Error message utilities:

    public static void error(String message) {
	log.noteError(message);
	throw new PascalError(message);
    }
	
    public static void error(int lineNum, String message) {
	error("Error in line " + lineNum + ": " + message);
    }

    private static void usage() {
	error("Usage: java -jar pascal2100.jar " +
	    "[-log{B|P|S|T|Y}] [-test{parser|scanner}] file");
    }

    public void panic(String where) {
	error("PANIC! Programming error in " + where);
    }

    public static void warning(String message) {
	log.noteError(message);
	System.err.println(message);
    }
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.constToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.typeToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.varToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.functionToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.procedureToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.beginToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.endToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Block extends PascalSyntax {
    public PascalDecl context;
	
	// public ConstDeclPart constDeclPart;
    // public TypeDeclPart typeDeclPart;
    // public VarDeclPart varDeclPart;
    // public FuncDecl[] funcDeclList;
    // public ProcDecl[] procDeclList;

    public StatmList stmtList;

    Block(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<block> on line " + lineNum;
    }

    public static Block parse(Scanner s) {
        enterParser("block");

        Block b = new Block(s.curLineNum());
        
        // Test for ConstDeclPart
        if (s.curToken.kind == constToken) {}
        
        // Test for TypeDeclPart
        if (s.curToken.kind == typeToken) {}
        
        // Test for VarDeclPart
        if (s.curToken.kind == varToken) {}
        
        // Test for function or procedure token
        while (s.curToken.kind == functionToken || s.curToken.kind == procedureToken) {}
        
        s.skip(beginToken);

        //b.stmtList = StatmList.parse(s);

        s.skip(endToken);
        
        leaveParser("block");

        return b;
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn("begin");
        Main.log.prettyIndent();
        
        //stmtList.prettyPrint();
        
        Main.log.prettyOutdent();
        Main.log.prettyPrintLn("end");
    }
}

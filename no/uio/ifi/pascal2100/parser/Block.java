package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.constToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.typeToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.varToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.functionToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.procedureToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.beginToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.endToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Block extends PascalSyntax {
    public PascalDecl context;

    public ConstDeclPart constDeclPart = null;
    public TypeDeclPart typeDeclPart = null;
    public VarDeclPart varDeclPart = null;
    public LinkedList<ProcDecl> procDeclList = new LinkedList<ProcDecl>();

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
        if (s.curToken.kind == constToken) {
            b.constDeclPart = ConstDeclPart.parse(s);
        }

        // Test for TypeDeclPart
        if (s.curToken.kind == typeToken) {
            b.typeDeclPart = TypeDeclPart.parse(s);
        }

        // Test for VarDeclPart
        if (s.curToken.kind == varToken) {
            b.varDeclPart = VarDeclPart.parse(s);
        }

        // Test for function or procedure token
        while (s.curToken.kind == functionToken || s.curToken.kind == procedureToken) {
            if (s.curToken.kind == functionToken) {
                b.procDeclList.add(FuncDecl.parse(s));
            } else {
                b.procDeclList.add(ProcDecl.parse(s));
            }
        }

        s.skip(beginToken);

        b.stmtList = StatmList.parse(s);

        s.skip(endToken);

        leaveParser("block");

        return b;
    }

    public void prettyPrint() {
        if (constDeclPart != null) {
            constDeclPart.prettyPrint();
        }

        if (typeDeclPart != null) {
            typeDeclPart.prettyPrint();
        }

        if (varDeclPart != null) {
            varDeclPart.prettyPrint();
        }

        if (!procDeclList.isEmpty()) {
            int i = 0;
            for (ProcDecl p : procDeclList) {
                if (i++ > 0) {
                    Main.log.prettyPrintLn();
                }

                p.prettyPrint();
            }
        }

        Main.log.prettyPrintLn();
        Main.log.prettyPrintLn();
        Main.log.prettyPrintLn("begin");
        Main.log.prettyIndent();

        stmtList.prettyPrint();

        Main.log.prettyOutdent();
        Main.log.prettyPrint("end");
    }
}

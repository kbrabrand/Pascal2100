package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.constToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.typeToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.varToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.functionToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.procedureToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.beginToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.endToken;

import java.util.HashMap;
import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Block extends PascalSyntax {
    public PascalDecl context;

    public ConstDeclPart constDeclPart = null;
    public TypeDeclPart typeDeclPart = null;
    public VarDeclPart varDeclPart = null;
    public LinkedList<ProcDecl> procDeclList = new LinkedList<ProcDecl>();

    Block outerScope = null;
    HashMap<String, PascalDecl> decls = new HashMap<String, PascalDecl>();

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

    public PascalDecl findDecl(String id, PascalSyntax where) {
        PascalDecl d = decls.get(id.toLowerCase());

        if (d != null) {
            Main.log.noteBinding(id, where, d);
            return d;
        }

        if (outerScope != null) {
            return outerScope.findDecl(id, where);
        }

        where.error("Name " + id + " is unknown!");

        return null; // Required by the Java compiler.
    }

    public void addDecl(String id, PascalDecl decl) {
        if (decls.containsKey(id.toLowerCase())) {
            decl.error(id + " declared twice in same block");
        }

        decls.put(id.toLowerCase(), decl);
    }

    /**
     * Shorthand method for setting outer scope when stepping into a new scope
     * 
     * @param outerScope
     * @param curScope
     * @param lib
     */
    public void check(Block outerScope, Block curScope, Library lib) {
        curScope.outerScope = outerScope;

        check(curScope, lib);
    }

    @Override
    public void check(Block curScope, Library lib) {
        // Constant declarations
        if (constDeclPart != null) {
            constDeclPart.check(this, lib);

            for (ConstDecl cd: constDeclPart.decls) {
                this.addDecl(cd.name, cd);
            }
        }

        // Type declarations
        if (typeDeclPart != null) {
            typeDeclPart.check(this, lib);

            for (TypeDecl td: typeDeclPart.decls) {
                this.addDecl(td.name.name, td);
            }
        }

        // Variable declarations
        if (varDeclPart != null) {
            varDeclPart.check(this, lib);

            for (VarDecl vd: varDeclPart.decls) {
                this.addDecl(vd.name, vd);
            }
        }

        // Procedure declarations
        for (ProcDecl pd : procDeclList) {
            pd.check(curScope, lib);
        }

        stmtList.check(this, lib);
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

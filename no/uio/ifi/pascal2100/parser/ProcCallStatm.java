package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.commaToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class ProcCallStatm extends Statement {
    public String name;
    public LinkedList<Expression> exprs = new LinkedList<Expression>();

    ProcCallStatm(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<proc call> " + this.getSourceLocation();
    }

    public static ProcCallStatm parse(Scanner s) {
        enterParser("proc call");

        s.test(nameToken);

        ProcCallStatm pcs = new ProcCallStatm(s.curToken.id, s.curLineNum());
        s.readNextToken();

        if (s.curToken.kind == leftParToken) {
            s.skip(leftParToken);

            while(true) {
                pcs.exprs.add(Expression.parse(s));

                if (s.curToken.kind != commaToken) {
                    break;
                }

                s.skip(commaToken);
            }
            
            s.skip(rightParToken);
        }

        leaveParser("proc call");

        return pcs;
    }

    @Override
    public void check(Block curScope, Library lib) {
        curScope.findDecl(name, this);

        for (Expression e : exprs) {
            e.check(curScope, lib);
        }
    }

    void prettyPrint() {
        Main.log.prettyPrint(name);

        if (!exprs.isEmpty()) {
            Main.log.prettyPrint("(");

            int i = 0;
            for (Expression e : exprs) {
                if (i++ > 0) {
                    Main.log.prettyPrint(", ");
                }

                e.prettyPrint();
            }

            Main.log.prettyPrint(")");
        }
    }

    private void genWriteCode(CodeFile f) {
        Expression e;
        String funcName = "";

        for (int i = 0; i < exprs.size(); i++) {
            e = exprs.get(i);

            if (e.isNumeric()) {
                e.genCode(f);
                funcName = "write_int";
            } else if (e.isChar()) {
                e.genCode(f);
                funcName = "write_char";
            } else if (e.isString()) {
                String label = f.getLocalLabel();
                f.genString(label, e.getString(), "");
                f.genInstr("", "leal", label + ",%eax", "Addr(\"" + e.getString() + "\")");
            } else {
                f.genInstr("", "", "", "write call, but not sure what type of value to write");
            }

            f.genInstr("", "pushl", "%eax", "Push param #" + (i + 1));
            f.genInstr("", "call", "write_string");
            f.genInstr("", "addl", "$4,%esp", "Pop parameter");
        }
    }

    public void genCode(CodeFile f) {
        System.out.println("fooo " + name);

        // Check this is a call on the library function write
        if (name.toLowerCase().equals("write")) {
            genWriteCode(f);
            return;
        }

        for (int i = exprs.size() -1; i > 0; i--) {
            
        }
    }
}

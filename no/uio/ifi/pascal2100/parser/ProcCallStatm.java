package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.commaToken;

import java.util.LinkedList;

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
        return "<proc call> on line " + lineNum;
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
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class ProcCallStatm extends Statement {
    public String name;
    public Expression expr = null;

    ProcCallStatm(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<proc-call-statm> on line " + lineNum;
    }

    public static ProcCallStatm parse(Scanner s) {
        enterParser("proc-call-statm");

        s.test(nameToken);

        ProcCallStatm pcs = new ProcCallStatm(s.curToken.id, s.curLineNum());
        s.readNextToken();

        if (s.curToken.kind == leftParToken) {
            s.skip(leftParToken);

            pcs.expr = Expression.parse(s);

            s.skip(rightParToken);
        }

        leaveParser("proc-call-statm");

        return pcs;
    }

    void prettyPrint() {
        Main.log.prettyPrint(name);
        
        if (expr != null) {
            Main.log.prettyPrint("(");
            expr.prettyPrint();
            Main.log.prettyPrint(")");
        }
    }
}

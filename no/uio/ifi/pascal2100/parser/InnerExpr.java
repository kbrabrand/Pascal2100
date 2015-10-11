package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class InnerExpr extends Factor {
    public Expression expr;

    InnerExpr(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<inner expr> on line " + lineNum;
    }

    public static InnerExpr parse(Scanner s) {
        enterParser("inner expr");

        s.skip(leftParToken);

        InnerExpr ie = new InnerExpr(s.curLineNum());

        ie.expr = Expression.parse(s);

        s.skip(rightParToken);

        leaveParser("inner expr");

        return ie;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint("(");

        expr.prettyPrint();

        Main.log.prettyPrint(")");
    }
}

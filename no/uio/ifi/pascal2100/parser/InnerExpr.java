package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class InnerExpr extends Factor {
    public Expression expr;

    InnerExpr(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<inner expr> " + this.getSourceLocation();
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

    public void check(Block curScope, Library lib, Expression e) {
        expr.check(curScope, lib, e);
    }

    @Override
    public void check(Block curScope, Library lib) {
        expr.check(curScope, lib);
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint("(");

        expr.prettyPrint();

        Main.log.prettyPrint(")");
    }

    @Override
    void genCode(CodeFile f) {
        expr.genCode(f);
    }
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.assignToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class AssignStatm extends Statement {
    public Variable var;
    public Expression expr;

    AssignStatm(String id, int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<assign statm> on line " + lineNum;
    }

    public static AssignStatm parse(Scanner s) {
        enterParser("assign-statm");

        AssignStatm as = new AssignStatm(s.curToken.id, s.curLineNum());

        as.var = Variable.parse(s);
        s.skip(assignToken);
        as.expr = Expression.parse(s);

        leaveParser("assign-statm");

        return as;
    }

    void prettyPrint() {
        var.prettyPrint();
        Main.log.prettyPrint(" := ");
        expr.prettyPrint();
    }
}

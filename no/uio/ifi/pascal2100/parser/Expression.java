package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;

public class Expression extends PascalSyntax {
    public SimpleExpr leading;
    public RelOperator relOperator = null;
    public SimpleExpr trailing = null;

    Expression(int n) {
        super(n);
    }

    public static Expression parse(Scanner s) {
        enterParser("expression");

        Expression e = new Expression(s.curLineNum());

        e.leading = SimpleExpr.parse(s);

        if (Operator.checkWhetherRelOperator(s.curToken)) {
            e.relOperator = RelOperator.parse(s);
            e.trailing = SimpleExpr.parse(s);
        }

        leaveParser("expression");

        return e;
    }

    @Override
    public String identify() {
        return "<expression> on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        leading.prettyPrint();

        if (relOperator != null) {
            relOperator.prettyPrint();
            trailing.prettyPrint();
        }
    }
}
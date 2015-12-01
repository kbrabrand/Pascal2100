package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Expression extends PascalSyntax {
    public SimpleExpr leading;
    public RelOperator relOperator = null;
    public SimpleExpr trailing = null;

    public boolean isNumeric = false;
    public boolean isString = false;
    public boolean isChar = false;
    public StringLiteral string;

    Expression(int n) {
        super(n);
    }

    @Override
    public String identify() {
        return "<expression> " + this.getSourceLocation();
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

    public boolean isString() {
        return this.isString;
    }

    public String getString() {
        return string.val;
    }

    public boolean isChar() {
        return this.isChar;
    }

    public boolean isNumeric() {
        return this.isNumeric;
    }

    public void check(Block curScope, Library lib, Expression e) {
        Expression checkExpr = e != null ? e : this;

        leading.check(curScope, lib, checkExpr);

        if (relOperator != null) {
            relOperator.check(curScope, lib, checkExpr);
            trailing.check(curScope, lib, checkExpr);
        }
    }

    @Override
    void prettyPrint() {
        leading.prettyPrint();

        if (relOperator != null) {
            Main.log.prettyPrint(" ");
            relOperator.prettyPrint();
            Main.log.prettyPrint(" ");
            trailing.prettyPrint();
        }
    }

    @Override
    void genCode(CodeFile f) {
        leading.genCode(f);

        if (trailing != null) {
            f.genInstr("", "pushl", "%eax");
            trailing.genCode(f);
            relOperator.genCode(f);
        }
    }
}
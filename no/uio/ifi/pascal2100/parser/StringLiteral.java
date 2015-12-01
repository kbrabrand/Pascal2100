package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class StringLiteral extends Constant {
    public String val;

    StringLiteral(String id, int lNum) {
        super(lNum);
        val = id;
    }

    @Override
    public String identify() {
        return "<string literal> " + val + " " + this.getSourceLocation();
    }

    public static StringLiteral parse(Scanner s) {
        enterParser("string literal");

        StringLiteral sl = new StringLiteral(s.curToken.strVal, s.curLineNum());

        leaveParser("string literal");

        s.readNextToken();

        return sl;
    }

    @Override
    public void check(Block curScope, Library lib, Expression e) {
        if (e != null) {
            e.isString = true;
            e.string = this;
        }
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint("'" + val + "'");
    }
}

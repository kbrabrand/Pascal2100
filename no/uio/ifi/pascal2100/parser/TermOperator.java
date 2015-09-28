package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class TermOperator extends Operator {
    public String name;

    TermOperator(String id, int lNum) {
        super(lNum);
        name = id;
    }

    public static TermOperator parse(Scanner s) {
        enterParser("term operator");

        if (!Operator.checkWhetherTermOperator(s.curToken)) {
            s.testError("term operator");
        }

        TermOperator t = new TermOperator(s.curToken.id, s.curLineNum());

        leaveParser("term operator");

        return t;
    }

    @Override
    public String identify() {
        return "<term operator> " + name + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(name);
    }
}

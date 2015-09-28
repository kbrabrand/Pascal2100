package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class FactorOperator extends Operator {
    public String name;

    FactorOperator(String id, int lNum) {
        super(lNum);
        name = id;
    }

    public static FactorOperator parse(Scanner s) {
        enterParser("factor-operator");

        if (!Operator.checkWhetherFactorOperator(s.curToken)) {
            s.testError("factor-operator");
        }

        FactorOperator fo = new FactorOperator(s.curToken.id, s.curLineNum());

        leaveParser("factor-operator");

        s.readNextToken();

        return fo;
    }

    @Override
    public String identify() {
        return "<factor operator> " + name + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(" " + name + " ");
    }
}
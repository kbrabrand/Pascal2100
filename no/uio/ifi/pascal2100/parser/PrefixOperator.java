package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class PrefixOperator extends Operator {
    public String name;

    PrefixOperator(String id, int lNum) {
        super(lNum);
        name = id;
    }

    public static PrefixOperator parse(Scanner s) {
        enterParser("prefix operator");

        if (!Operator.checkWhetherPrefixOperator(s.curToken)) {
            s.testError("prefix operator");
        }

        PrefixOperator t = new PrefixOperator(s.curToken.id, s.curLineNum());

        leaveParser("prefix operator");

        s.readNextToken();

        return t;
    }

    @Override
    public String identify() {
        return "<prefix operator> " + name + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(name);
    }
}

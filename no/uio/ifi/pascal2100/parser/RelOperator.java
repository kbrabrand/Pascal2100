package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class RelOperator extends Operator {
    public String name;

    RelOperator(String id, int lNum) {
        super(lNum);
        name = id;
    }

    public static RelOperator parse(Scanner s) {
        enterParser("rel-operator");

        if (!Operator.checkWhetherRelOperator(s.curToken)) {
            s.testError("relation operator");
        }

        RelOperator r = new RelOperator(s.curToken.id, s.curLineNum());

        leaveParser("rel-operator");

        s.readNextToken();
        
        return r;
    }
    
    @Override
    public String identify() {
        return "<rel operator> " + name + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(" " + name + " ");
    }
}

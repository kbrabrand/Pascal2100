package no.uio.ifi.pascal2100.parser;

import java.util.Iterator;
import java.util.Stack;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Term extends Operator {
    Stack<Factor> factors = new Stack<Factor>();
    Stack<FactorOperator> factorOpers = new Stack<FactorOperator>();

    Term(int lNum) {
        super(lNum);
    }

    static Term parse(Scanner s) {
        enterParser("term");

        Term t = new Term(s.curLineNum());

        while(true) {
            Factor f = Factor.parse(s);

            t.factors.push(f);

            // Break the loop if the current token after parsing the
            // factor is not a factor operator
            if (!Operator.checkWhetherFactorOperator(s.curToken)) {
               break;
            }

            t.factorOpers.push(FactorOperator.parse(s));
        }

        leaveParser("term");

        return t;
    }

    @Override
    public String identify() {
        return "<term> on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Iterator<Factor> factorsIter = factors.iterator();
        Iterator<FactorOperator> factorOpersIter = factorOpers.iterator();

        while (factorsIter.hasNext()) {
            factorsIter.next().prettyPrint();
            Main.log.prettyPrint(" ");

            if (factorOpersIter.hasNext()) {
                factorOpersIter.next().prettyPrint();
                Main.log.prettyPrint(" ");
            }
        }
    }
}

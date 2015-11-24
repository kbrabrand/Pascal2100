package no.uio.ifi.pascal2100.parser;

import java.util.Iterator;
import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Term extends Operator {
    LinkedList<Factor> factors = new LinkedList<Factor>();
    LinkedList<FactorOperator> factorOpers = new LinkedList<FactorOperator>();

    Term(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<term> " + this.getSourceLocation();
    }

    static Term parse(Scanner s) {
        enterParser("term");

        Term t = new Term(s.curLineNum());

        while(true) {
            Factor f = Factor.parse(s);

            t.factors.add(f);

            // Break the loop if the current token after parsing the
            // factor is not a factor operator
            if (!Operator.checkWhetherFactorOperator(s.curToken)) {
               break;
            }

            t.factorOpers.add(FactorOperator.parse(s));
        }

        leaveParser("term");

        return t;
    }

    @Override
    public void check(Block curScope, Library lib) {
        for (Factor f : factors) {
            f.check(curScope, lib);
        }
    }

    @Override
    void prettyPrint() {
        Iterator<FactorOperator> factorOpersIter = factorOpers.iterator();

        int counter = 0;
        for (Factor f : factors) {
            if (counter++  > 0) {
                Main.log.prettyPrint(" ");
            }

            f.prettyPrint();

            if (factorOpersIter.hasNext()) {
                Main.log.prettyPrint(" ");
                factorOpersIter.next().prettyPrint();
            }
        }
    }

    @Override
    void genCode(CodeFile f) {
        Iterator<FactorOperator> factorOpersIter = factorOpers.iterator();

        for (Factor factor : factors) {
            factor.genCode(f);

            if (factorOpersIter.hasNext()) {
                factorOpersIter.next().genCode(f);
            }
        }
    }
}

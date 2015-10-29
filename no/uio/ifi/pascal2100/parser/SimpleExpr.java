package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

import java.util.Iterator;
import java.util.Stack;

public class SimpleExpr extends PascalSyntax {
    public PrefixOperator prefOper = null;
    public Stack<Term> terms = new Stack<Term>();
    public Stack<TermOperator> termOpers = new Stack<TermOperator>();

    SimpleExpr(int n) {
        super(n);
    }

    @Override
    public String identify() {
        return "<simple expr> on line " + lineNum;
    }

    public static SimpleExpr parse(Scanner s) {
        enterParser("simple expr");

        SimpleExpr se = new SimpleExpr(s.curLineNum());

        if (Operator.checkWhetherPrefixOperator(s.curToken)) {
            se.prefOper = PrefixOperator.parse(s);
        }

        while(true) {
            se.terms.add(Term.parse(s));

            // Stop iteration if the token after the term is not a term operator
            if(!Operator.checkWhetherTermOperator(s.curToken)) {
                break;
            }

            se.termOpers.add(TermOperator.parse(s));
        }

        leaveParser("simple expr");

        return se;
    }

    @Override
    public void check(Block curScope, Library lib) {
        for (Term t : terms) {
            t.check(curScope, lib);
        }
    }

    @Override
    void prettyPrint() {
        Iterator<TermOperator> termOpersIter = termOpers.iterator();

        if (prefOper != null) {
            prefOper.prettyPrint();
        }

        int counter = 0;
        for (Term t : terms) {
            if (counter++ > 0) {
                Main.log.prettyPrint(" ");
            }

            t.prettyPrint();

            if (termOpersIter.hasNext()) {
                Main.log.prettyPrint(" ");
                termOpersIter.next().prettyPrint();
            }
        }
    }
}

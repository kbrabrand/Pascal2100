package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

import java.util.Iterator;
import java.util.Stack;

public class SimpleExpr extends PascalSyntax {
    public PrefixOperator prefOper = null;
    public Stack<Term> terms;
    public Stack<TermOperator> termOpers;

    SimpleExpr(int n) {
        super(n);
    }

    public static SimpleExpr parse(Scanner s) {
        enterParser("simple-expr");

        SimpleExpr se = new SimpleExpr(s.curLineNum());

        if (Operator.checkWhetherPrefixOperator(s.curToken)) {
        	se.prefOper = PrefixOperator.parse(s);
        }

        while(true) {
        	se.terms.push(Term.parse(s));

        	// Stop iteration if the token after the term is not a term operator
            if(!Operator.checkWhetherTermOperator(s.curToken)) {
                break;
            }

        	se.termOpers.push(TermOperator.parse(s));
        }
        
        leaveParser("simple-expr");
        
        return se;
    }

    @Override
    public String identify() {
        return "<simple-expr> on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Iterator<Term> termsIter = terms.iterator();
        Iterator<TermOperator> termOpersIter = termOpers.iterator();

        if (prefOper != null) {
            prefOper.prettyPrint();
            Main.log.prettyPrint(" ");
        }

        while (termsIter.hasNext()) {
            termsIter.next().prettyPrint();
            Main.log.prettyPrint(" ");
            
            if (termOpersIter.hasNext()) {
            	termOpersIter.next().prettyPrint();
            	Main.log.prettyPrint(" ");
            }
    	}
    }
}

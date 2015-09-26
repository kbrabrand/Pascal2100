package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.addToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.subtractToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.orToken;

import java.util.Iterator;
import java.util.Stack;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Term extends Operator {
    public Stack<Factor> factors;
    public Stack<FactorOperator> factorOpers;

    Term(int lNum) {
        super(lNum);
    }

    public static Term parse(Scanner s) {
        enterParser("term");

        Term t = new Term(s.curLineNum());

        while(true) {
            t.factors.push(Factor.parse(s));
            
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

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;

import java.util.Iterator;
import java.util.Stack;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class StatmList extends PascalSyntax {
    public Stack<Statement> statements;
	
    StatmList(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<stmt-list> on line " + lineNum;
    }

    public static StatmList parse(Scanner s) {
        enterParser("statm-list");

        StatmList sl = new StatmList(s.curLineNum());
        
        while(true) {
            sl.statements.push(Statement.parse(s));
            
            // Skip semicolon if we have one, break the loop if not
            if (s.curToken.kind == semicolonToken) {
            	s.skip(semicolonToken);
            } else {
            	break;
            }
        }
        
        leaveParser("statm-list");

        return sl;
    }
    
    void prettyPrint() {
    	Iterator<Statement> statmIter = statements.iterator();
    	
    	while (statmIter.hasNext()) {
    		statmIter.next().prettyPrint();
    		
    		if (statmIter.hasNext()) {
    			Main.log.prettyPrint(";");
    		}
    	}
    }
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.beginToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.ifToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.procedureToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.whileToken;

import java.util.Stack;

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

        StatmList list = new StatmList(s.curLineNum());
        
        // Test for function or procedure token
        boolean ok = true;
        
        while(true) {
        	// Assign statement
        	if (s.curToken.kind == nameToken) {
        		
        	}
        	
        	// Compound statement
        	else if (s.curToken.kind == beginToken) {
        		
        	}
        	
        	// Empty statement
        	else if (s.curToken.kind == semicolonToken) {
        		
        	}
        	
        	// If-statement
        	else if (s.curToken.kind == ifToken) {
        		
        	}
        	
        	// Procedure call
        	else if (s.curToken.kind == procedureToken) {
        		
        	}
        	
        	// While-statement
        	else if (s.curToken.kind == whileToken) {
        		
        	}
        	
        	// Not a statement. Stop looping
        	else {
        		break;
        	}
        	
        	list.statements.push(statement)
        }
        
        s.skip(beginToken);

        b.stmtList = StatmList.parse(s);
        b.stmtList.context = b;

        s.skip(endToken);

        leaveParser("statm-list");

        return b;
    }
    
    void prettyPrint() {
        // TODO Auto-generated method stub
    }
}

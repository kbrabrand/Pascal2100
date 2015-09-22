package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.equalToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.notEqualToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.lessToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.lessEqualToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.greaterToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.greaterEqualToken;

import no.uio.ifi.pascal2100.scanner.Scanner;

public class Expression extends PascalSyntax {
    public SimpleExpr leading;
    public RelOperator relOperator;
    public SimpleExpr trailing;
    
    Expression(int n) {
        super(n);
    }

    public static Expression parse(Scanner s) {
        enterParser("expression");
        
        Expression e = new Expression(s.curLineNum());
        
        e.leading = SimpleExpr.parse(s);
        s.readNextToken();
        
        if (
            s.curToken.kind == equalToken ||
            s.curToken.kind == notEqualToken ||
            s.curToken.kind == lessToken||
            s.curToken.kind == lessEqualToken ||
            s.curToken.kind == greaterToken||
            s.curToken.kind == greaterEqualToken
        ) {
            e.relOperator = RelOperator.parse(s);
            e.trailing = SimpleExpr.parse(s);
        }
        
        leaveParser("expression");
        
        return e;
    }
    
    @Override
    public String identify() {
        return "<expression> on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        // TODO Auto-generated method stub
    }
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.assignToken;

import no.uio.ifi.pascal2100.scanner.Scanner;


public class AssignStatm extends Statement {
    public String name;
    public Expression expression;

    AssignStatm(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<assign statm> on line " + lineNum;
    }

    public static AssignStatm parse(Scanner s) {
        enterParser("assign-statm");

        s.test(nameToken);

        AssignStatm a = new AssignStatm(s.curToken.id, s.curLineNum());
        s.readNextToken();
        
        s.skip(assignToken);
        
        a.expression = Expression.parse(s);
        
        leaveParser("assign-statm");
        
        return a;
    }

    void prettyPrint() {
        // TODO Auto-generated method stub
    }
}

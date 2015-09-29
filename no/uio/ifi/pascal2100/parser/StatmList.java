package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.scanner.Scanner;

public class StatmList extends PascalSyntax {
    public LinkedList<Statement> statements = new LinkedList<Statement>();

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
            sl.statements.add(Statement.parse(s));

            // Skip semicolon if we have one, break the loop if not
            if (s.curToken.kind != semicolonToken) {
                break;
            }
        }

        leaveParser("statm-list");

        return sl;
    }

    void prettyPrint() {
        for (Statement s : statements) {
           s.prettyPrint();
        }
    }
}

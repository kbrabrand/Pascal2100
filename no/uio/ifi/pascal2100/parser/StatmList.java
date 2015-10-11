package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.endToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class StatmList extends PascalSyntax {
    public LinkedList<Statement> statements = new LinkedList<Statement>();

    StatmList(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<stmt list> on line " + lineNum;
    }

    public static StatmList parse(Scanner s) {
        enterParser("statm list");

        StatmList sl = new StatmList(s.curLineNum());

        while(true) {
            sl.statements.add(Statement.parse(s));
            
            // Break the loop if token is not a semicolon token 
            if (s.curToken.kind != semicolonToken) {
                break;
            }
            
            // Parse empty list and break loop if semicolon and next is end
            if (s.curToken.kind == semicolonToken && s.nextToken.kind == endToken) {
                sl.statements.add(Statement.parse(s));
                break;
            }
            
            s.skip(semicolonToken);
        }
        
        leaveParser("statm list");

        return sl;
    }

    void prettyPrint() {
        int i = 0;
        for (Statement s : statements) {
            if (i++ > 0 && !(s instanceof EmptyStatm)) {
                Main.log.prettyPrintLn(";");
            }
            s.prettyPrint();
        }
        Main.log.prettyPrintLn();
    }
}

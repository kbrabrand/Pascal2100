package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.commaToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class FuncCall extends Factor {
    public String name;
    public LinkedList<Expression> exprs = new LinkedList<Expression>();

    FuncCall(String id, int lNum) {
        super(lNum);
        name = id;
    }

    public static FuncCall parse(Scanner s) {
        enterParser("func-call");

        s.test(nameToken);

        FuncCall fc = new FuncCall(s.curToken.id, s.curLineNum());

        s.readNextToken();

        if (s.curToken.kind == leftParToken) {
            s.skip(leftParToken);
            
            while (true) {
                fc.exprs.push(Expression.parse(s));

                if (s.curToken.kind != commaToken) {
                    break;
                }

                s.skip(commaToken);
            }

            s.skip(rightParToken);
        }

        leaveParser("func-call");
        
        return fc;
    }

    @Override
    public String identify() {
    	return "<func-call> on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(name);

        if (exprs.size() > 0) {
            Main.log.prettyPrint("(");

            int i = 0;
            for (Expression e : exprs) {
                if (i++ > 0) {
                    Main.log.prettyPrint(", ");
                }

                e.prettyPrint();
            }

            Main.log.prettyPrint(")");
        }
    }
}

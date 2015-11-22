package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftBracketToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightBracketToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Variable extends Factor {
    public String name;
    public Expression expr = null;
    public PascalDecl nameDecl = null;

    Variable(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<variable> " + this.getSourceLocation();
    }

    public static Variable parse(Scanner s) {
        enterParser("variable");

        s.test(nameToken);

        Variable v = new Variable(s.curToken.id, s.curLineNum());

        s.readNextToken();
        
        if (s.curToken.kind == leftBracketToken) {
            s.skip(leftBracketToken);

            v.expr = Expression.parse(s);

            s.skip(rightBracketToken);
        }

        leaveParser("variable");

        return v;
    }

    @Override
    public void check(Block curScope, Library lib) {
        nameDecl = curScope.findDecl(name, this);

        if (expr != null) {
            expr.check(curScope, lib);
        }
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(name);

        if (expr != null) {
            Main.log.prettyPrint("[");
            expr.prettyPrint();
            Main.log.prettyPrint("]");
        }
    }
}

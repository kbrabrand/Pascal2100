package no.uio.ifi.pascal2100.parser;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;
import static no.uio.ifi.pascal2100.scanner.TokenKind.commaToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

public class EnumType extends Type {
    public LinkedList<EnumLiteral> literals = new LinkedList<EnumLiteral>();

    EnumType(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<enum type> " + this.getSourceLocation();
    }

    public static EnumType parse(Scanner s) {
        enterParser("enum type");

        EnumType et = new EnumType(s.curLineNum());

        s.skip(leftParToken);

        do {
            et.literals.add(EnumLiteral.parse(s));

            if (s.curToken.kind == commaToken) {
                s.skip(commaToken);
            }
        } while (s.curToken.kind == nameToken);

        s.skip(rightParToken);

        leaveParser("enum type");

        return et;
    }

    public void check(Block curScope, Library lib, Expression e) {
        EnumLiteral el;

        for (int i = 0; i < literals.size(); i++) {
            el = literals.get(i);

            el.check(curScope, lib, e);
            el.index = i;

            curScope.addDecl(el.name, el);
        }

        if (e != null) {
            e.isNumeric = true;
        }
    }

    void prettyPrint() {
        Main.log.prettyPrint("(");

        int i = 0;
        for (EnumLiteral el : literals) {
            if (i++ > 0) {
                Main.log.prettyPrint(", ");
            }

            el.prettyPrint();
        }

        Main.log.prettyPrint(")");
    }
}

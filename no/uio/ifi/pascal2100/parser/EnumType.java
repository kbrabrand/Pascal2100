package no.uio.ifi.pascal2100.parser;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
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
        } while (s.curToken.kind == nameToken);

        s.skip(leftParToken);

        leaveParser("enum type");

        return et;
    }

    public void check(Block curScope, Library lib, Expression e) {
        check(curScope, lib);

        e.isNumeric = true;
    }

    @Override
    public void check(Block curScope, Library lib) {
        for (EnumLiteral el : literals) {
            el.check(curScope, lib);
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

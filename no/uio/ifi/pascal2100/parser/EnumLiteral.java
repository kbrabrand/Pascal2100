package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class EnumLiteral extends PascalDecl {
    EnumLiteral(String id, int lNum) {
        super(id, lNum);
    }

    @Override
    public String identify() {
        return "<enum literal> " + this.getSourceLocation();
    }

    public static EnumLiteral parse(Scanner s) {
        enterParser("enum literal");

        s.test(nameToken);
        EnumLiteral cd = new EnumLiteral(s.curToken.id, s.curLineNum());

        leaveParser("enum literal");

        s.readNextToken();

        return cd;
    }

    @Override
    public void check(Block curScope, Library lib) { }

    public void prettyPrint() {
        Main.log.prettyPrint(name);
    }

    @Override
    public int getSize() {
        return 4;
    }
}

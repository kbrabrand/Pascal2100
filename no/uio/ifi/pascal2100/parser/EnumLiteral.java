package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class EnumLiteral extends PascalDecl {
    public Constant constant;

    EnumLiteral(String id, int lNum) {
        super(id, lNum);
    }

    @Override
    public String identify() {
        return "<enum-literal> on line " + lineNum;
    }

    public static EnumLiteral parse(Scanner s) {
        enterParser("enum-decl");

        s.test(nameToken);
        EnumLiteral cd = new EnumLiteral(s.curToken.id, s.curLineNum());

        leaveParser("enum-decl");

        s.readNextToken();

        return cd;
    }

    public void prettyPrint() {
        Main.log.prettyPrint(name);
    }
}

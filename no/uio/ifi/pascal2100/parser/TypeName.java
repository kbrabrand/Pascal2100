package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class TypeName extends Type {
    public String name;

    TypeName(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<type name> " + name + " on line " + lineNum;
    }

    public static TypeName parse(Scanner s) {
        enterParser("type name");

        s.test(nameToken);

        TypeName tn = new TypeName(s.curToken.id, s.curLineNum());
        s.readNextToken();

        leaveParser("type name");

        return tn;
    }

    void prettyPrint() {
        Main.log.prettyPrint(name);
    }
}

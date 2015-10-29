package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class NamedConst extends Constant {
    public String name;

    NamedConst(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<named const> " + name + " on line " + lineNum;
    }

    public static NamedConst parse(Scanner s) {
        enterParser("named const");

        s.test(nameToken);

        NamedConst nc = new NamedConst(s.curToken.id, s.curLineNum());

        leaveParser("named const");

        s.readNextToken();

        return nc;
    }

    @Override
    public void check(Block curScope, Library lib) {
        curScope.findDecl(name, this);
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(name);
    }
}

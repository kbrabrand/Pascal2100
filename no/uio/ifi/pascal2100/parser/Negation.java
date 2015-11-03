package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.notToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Negation extends Factor {
    public Factor factor;;

    Negation(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<negation> " + this.getSourceLocation();
    }

    public static Negation parse(Scanner s) {
        enterParser("negation");

        s.skip(notToken);

        Negation n = new Negation(s.curLineNum());

        n.factor = Factor.parse(s);

        leaveParser("negation");

        return n;
    }

    @Override
    public void check(Block curScope, Library lib) {
        factor.check(curScope, lib);
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint("not ");

        factor.prettyPrint();
    }
}

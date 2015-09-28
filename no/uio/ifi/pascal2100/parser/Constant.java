package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;

abstract class Constant extends Factor {
	Constant(int lNum) {
        super(lNum);
    }

    static Constant parse(Scanner s) {
        enterParser("constant");

        Constant c = null;

        switch (s.curToken.kind) {
            case intValToken:
                c = NumberLiteral.parse(s);
                break;
            case stringValToken:
                c = StringLiteral.parse(s);
                break;
            default:
                c = NamedConst.parse(s);
        }

        leaveParser("constant");
        return c;
    }
}

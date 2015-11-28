package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;

abstract class Factor extends PascalSyntax {
    Factor(int lNum) {
        super(lNum);
    }

    static Factor parse(Scanner s) {
        enterParser("factor");

        Factor f = null;

        switch (s.curToken.kind) {
            case leftParToken:
                f = InnerExpr.parse(s);
                break;
            case stringValToken:
            case intValToken:
                f = Constant.parse(s);
                break;
            case notToken:
                f = Negation.parse(s);
                break;
            case nameToken:
                switch (s.nextToken.kind) {
                    case leftParToken:
                        f = FuncCall.parse(s);
                        break;
                    default:
                        f = Variable.parse(s);
                        break;
                }
                break;
            default:
                f = Negation.parse(s);
                break;
        }

        leaveParser("factor");

        return f;
    }

    abstract void check(Block curScope, Library lib, Expression e);
}
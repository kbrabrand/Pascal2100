package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;

abstract class Statement extends PascalSyntax {
    Statement(int lNum) {
        super(lNum);
    }

    static Statement parse(Scanner s) {
        enterParser("statement");

        Statement st = null;
        switch (s.curToken.kind) {
            case ifToken:
                st = IfStatm.parse(s);
                break;
            case nameToken:
                switch (s.nextToken.kind) {
                    case assignToken:
                    case leftBracketToken:
                        st = AssignStatm.parse(s);
                        break;
                    default:
                        st = ProcCallStatm.parse(s);
                        break;
                } break;
            case whileToken:
                st = WhileStatm.parse(s);
                break;
            case beginToken:
                st = CompoundStatm.parse(s);
                break;
            default:
                st = EmptyStatm.parse(s); 
                break;
        }

        leaveParser("statement");
        return st;
    }
}

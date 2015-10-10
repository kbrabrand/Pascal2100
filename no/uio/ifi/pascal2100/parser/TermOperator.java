package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class TermOperator extends Operator {
    public TokenKind kind;

    TermOperator(TokenKind kind, int lNum) {
        super(lNum);
        this.kind = kind;
    }

    public static TermOperator parse(Scanner s) {
        enterParser("term operator");

        if (!Operator.checkWhetherTermOperator(s.curToken)) {
            s.testError("term operator");
        }

        TermOperator t = new TermOperator(s.curToken.kind, s.curLineNum());

        s.readNextToken();

        leaveParser("term operator");

        return t;
    }

    @Override
    public String identify() {
        return "<term operator> " + kind.identify() + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        String symbol;
        
        switch (kind) {
            case addToken:
                symbol = "+";
                break;
            case subtractToken:
                symbol = "-";
                break;
            default:
                symbol = "or";
                break;
        }

        Main.log.prettyPrint(symbol);
    }
}

package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class PrefixOperator extends Operator {
    public TokenKind kind;

    PrefixOperator(TokenKind kind, int lNum) {
        super(lNum);
        this.kind = kind;
    }

    public static PrefixOperator parse(Scanner s) {
        enterParser("prefix operator");

        if (!Operator.checkWhetherPrefixOperator(s.curToken)) {
            s.testError("prefix operator");
        }

        PrefixOperator t = new PrefixOperator(s.curToken.kind, s.curLineNum());

        leaveParser("prefix operator");

        s.readNextToken();

        return t;
    }

    @Override
    public String identify() {
        return "<prefix operator> " + kind.identify() + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        String symbol;

        switch (kind) {
            case addToken:
                symbol = "+";
                break;
            default:
                symbol = "-";
                break;
        }

        Main.log.prettyPrint(symbol);
    }
}

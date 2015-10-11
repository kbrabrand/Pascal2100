package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class RelOperator extends Operator {
    public TokenKind kind;

    RelOperator(TokenKind kind, int lNum) {
        super(lNum);
        this.kind = kind;
    }

    @Override
    public String identify() {
        return "<rel oper> " + kind.identify() + " on line " + lineNum;
    }

    public static RelOperator parse(Scanner s) {
        enterParser("rel oper");

        if (!Operator.checkWhetherRelOperator(s.curToken)) {
            s.testError("relation operator");
        }

        RelOperator r = new RelOperator(s.curToken.kind, s.curLineNum());

        leaveParser("rel oper");

        s.readNextToken();

        return r;
    }

    @Override
    void prettyPrint() {
        String symbol;

        switch (kind) {
            case equalToken:
                symbol = "=";
                break;
            case notEqualToken:
                symbol = "<>";
                break;
            case lessToken:
                symbol = "<";
                break;
            case lessEqualToken:
                symbol = "<=";
                break;
            case greaterToken:
                symbol = ">";
                break;
            default:
                symbol = ">=";
                break;
        }

        Main.log.prettyPrint(symbol);
    }
}

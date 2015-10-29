package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class FactorOperator extends Operator {
    public TokenKind kind;

    FactorOperator(TokenKind kind, int lNum) {
        super(lNum);
        this.kind = kind;
    }

    @Override
    public String identify() {
        return "<factor oper> " + kind.identify() + " on line " + lineNum;
    }

    public static FactorOperator parse(Scanner s) {
        enterParser("factor oper");

        if (!Operator.checkWhetherFactorOperator(s.curToken)) {
            s.testError("factor oper");
        }

        FactorOperator fo = new FactorOperator(s.curToken.kind, s.curLineNum());

        leaveParser("factor oper");

        s.readNextToken();

        return fo;
    }

    @Override
    public void check(Block curScope, Library lib) { }

    @Override
    void prettyPrint() {
        String symbol;

        switch (kind) {
            case multiplyToken:
                symbol = "*";
                break;
            case divToken:
                symbol = "div";
                break;
            case modToken:
                symbol = "mod";
                break;
            default:
                symbol = "and";
                break;
        }

        Main.log.prettyPrint(symbol);
    }
}
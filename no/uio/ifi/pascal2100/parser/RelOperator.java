package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
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
        return "<rel oper> " + kind.identify() + " " + this.getSourceLocation();
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
    public void check(Block curScope, Library lib, Expression e) { }

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

    @Override
    void genCode(CodeFile f) {
        f.genInstr("", "popl", "%ecx");
        f.genInstr("", "cmpl", "%eax,%ecx");
        f.genInstr("", "movl", "$0,%eax");

        switch (kind) {
            case equalToken:
                f.genInstr("", "sete", "%al", "Test =");
                break;
            case notEqualToken:
                f.genInstr("", "setne", "%al", "Test <>");
                break;
            case lessToken:
                f.genInstr("", "setl", "%al", "Test <");
                break;
            case lessEqualToken:
                f.genInstr("", "setle", "%al", "Test <=");
                break;
            case greaterToken:
                f.genInstr("", "setg", "%al", "Test >");
                break;
            default:
                f.genInstr("", "setge", "%al", "Test >=");
                break;
        }
	}
}

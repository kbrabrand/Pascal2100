package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class TermOperator extends Operator {
    public TokenKind kind;

    TermOperator(TokenKind kind, int lNum) {
        super(lNum);
        this.kind = kind;
    }

    @Override
    public String identify() {
        return "<term oper> " + kind.identify() + " " + this.getSourceLocation();
    }

    public static TermOperator parse(Scanner s) {
        enterParser("term oper");

        if (!Operator.checkWhetherTermOperator(s.curToken)) {
            s.testError("term oper");
        }

        TermOperator t = new TermOperator(s.curToken.kind, s.curLineNum());

        s.readNextToken();

        leaveParser("term oper");

        return t;
    }

    @Override
    public void check(Block curScope, Library lib) { }

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

    @Override
    void genCode(CodeFile f) {
        switch (kind) {
            case addToken:
                f.genInstr("", "movl", "%eax,%ecx");
                f.genInstr("", "popl", "%eax");
                f.genInstr("", "addl", "%ecx,%eax", "+");
                break;
            case subtractToken:
                f.genInstr("", "movl", "%eax,%ecx");
                f.genInstr("", "popl", "%eax");
                f.genInstr("", "subl", "%ecx,%eax", "-");
                break;
            default:
                f.genInstr("", "", "", kind.name());
                break;
        }
    }
}

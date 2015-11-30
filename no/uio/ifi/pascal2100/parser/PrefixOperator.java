package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;
import static no.uio.ifi.pascal2100.scanner.TokenKind.subtractToken;

public class PrefixOperator extends Operator {
    public TokenKind kind;

    PrefixOperator(TokenKind kind, int lNum) {
        super(lNum);
        this.kind = kind;
    }

    @Override
    public String identify() {
        return "<prefix oper> " + kind.identify() + " " + this.getSourceLocation();
    }

    public static PrefixOperator parse(Scanner s) {
        enterParser("prefix oper");

        if (!Operator.checkWhetherPrefixOperator(s.curToken)) {
            s.testError("prefix oper");
        }

        PrefixOperator t = new PrefixOperator(s.curToken.kind, s.curLineNum());

        leaveParser("prefix oper");

        s.readNextToken();

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
            default:
                symbol = "-";
                break;
        }

        Main.log.prettyPrint(symbol);
    }

    @Override
    void genCode(CodeFile f) {
        if (kind != subtractToken) {
            return;
        }

        f.genInstr("", "negl", "%eax", "- (prefix)");
    }
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class EmptyStatm extends Statement {
    EmptyStatm(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<empty statm> " + this.getSourceLocation();
    }

    public static EmptyStatm parse(Scanner s) {
        EmptyStatm es = new EmptyStatm(s.curLineNum());

        enterParser("empty statm");

        s.skip(semicolonToken);

        leaveParser("empty statm");

        return es;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(";");
    }
}

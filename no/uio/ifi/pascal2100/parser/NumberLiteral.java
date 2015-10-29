package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.intValToken;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class NumberLiteral extends Constant {
    public int val;

    NumberLiteral(int val, int lNum) {
        super(lNum);
        this.val = val;
    }

    @Override
    public String identify() {
        return "<number literal> " + val + " on line " + lineNum;
    }

    public static NumberLiteral parse(Scanner s) {
        enterParser("number literal");

        s.test(intValToken);
        
        NumberLiteral nl = new NumberLiteral(s.curToken.intVal, s.curLineNum());

        leaveParser("number literal");

        s.readNextToken();

        return nl;
    }

    @Override
    public void check(Block curScope, Library lib) { }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(Integer.toString(val));
    }
}

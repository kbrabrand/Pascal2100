package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.intValToken;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class NumberLiteral extends Constant {
    public int val;

    NumberLiteral(String id, int lNum) {
        super(lNum);
        val = Integer.parseInt(id);
    }
    
    public static NumberLiteral parse(Scanner s) {
        enterParser("number-literal");

        s.test(intValToken);
        
        NumberLiteral nl = new NumberLiteral(s.curToken.id, s.curLineNum());

        leaveParser("number-literal");

        s.readNextToken();

        return nl;
    }

    @Override
    public String identify() {
    	return "<number-literal> " + val + " on line " + lineNum;
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(Integer.toString(val));
    }
}

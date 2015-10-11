package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

import static no.uio.ifi.pascal2100.scanner.TokenKind.arrayToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftBracketToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightBracketToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.ofToken;

public class ArrayType extends Type {
    public Type type;
    public Type ofType;

    ArrayType(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<array-type> on line " + lineNum;
    }

    public static ArrayType parse(Scanner s) {
        enterParser("array-type");

        ArrayType at = new ArrayType(s.curLineNum());

        s.skip(arrayToken);
        s.skip(leftBracketToken);

        at.type = Type.parse(s);

        s.skip(rightBracketToken);
        s.skip(ofToken);
        
        at.ofType = Type.parse(s);

        leaveParser("array-type");

        return at;
    }

    void prettyPrint() {
        Main.log.prettyPrint("array [");
        type.prettyPrint();
        Main.log.prettyPrint("] of ");
        ofType.prettyPrint();
    }
}
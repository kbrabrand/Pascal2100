package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

import no.uio.ifi.pascal2100.parser.NumberLiteral;
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
        return "<array type> " + this.getSourceLocation();
    }

    public static ArrayType parse(Scanner s) {
        enterParser("array type");

        s.skip(arrayToken);

        ArrayType at = new ArrayType(s.curLineNum());

        s.skip(leftBracketToken);

        at.type = Type.parse(s);

        s.skip(rightBracketToken);
        s.skip(ofToken);
        
        at.ofType = Type.parse(s);

        leaveParser("array type");

        return at;
    }

    private RangeType getRange() {
        Type curType = type;

        while (!(curType instanceof RangeType)) {
            curType = ((TypeName) curType).decl.type;
        }
        
        return (RangeType) curType;
    }

    public int getLow() {
        NumberLiteral low = (NumberLiteral) getRange().from;
        return low.val;
    }

    public int getHigh() {
        NumberLiteral high = (NumberLiteral) getRange().to;
        return high.val;
    }

    @Override
    public void check(Block curScope, Library lib) {
        type.check(curScope, lib);
        ofType.check(curScope, lib);
    }

    void prettyPrint() {
        Main.log.prettyPrint("array [");
        type.prettyPrint();
        Main.log.prettyPrint("] of ");
        ofType.prettyPrint();
    }

    @Override
    void genCode(CodeFile f) {
        // TODO Auto-generated method stub
    }
}

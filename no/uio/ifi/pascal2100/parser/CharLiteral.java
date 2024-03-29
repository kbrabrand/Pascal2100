package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class CharLiteral extends Constant {
    public char val;

    CharLiteral(char id, int lNum) {
        super(lNum);
        val = id;
    }

    @Override
    public String identify() {
        return "<char literal> " + val + " " + this.getSourceLocation();
    }

    public static CharLiteral parse(Scanner s) {
        enterParser("char literal");

        CharLiteral sl = new CharLiteral(s.curToken.strVal.charAt(0), s.curLineNum());

        leaveParser("char literal");

        s.readNextToken();

        return sl;
    }

    @Override
    public void check(Block curScope, Library lib, Expression e) {
        if (e != null) {
            e.isChar = true;
        }
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint("'" + String.valueOf(val) + "'");
    }

    @Override
    void genCode(CodeFile f) {
        int intVal = (int) val;

        f.genInstr("", "movl", "$" + intVal + ",%eax", "  char " + intVal);
    }
}

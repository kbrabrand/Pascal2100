package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.beginToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.endToken;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class CompoundStatm extends Statement {
    public StatmList stmtList;

    CompoundStatm(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<compound statm> " + this.getSourceLocation();
    }

    public static CompoundStatm parse(Scanner s) {
        enterParser("compound statm");

        CompoundStatm c = new CompoundStatm(s.curLineNum());

        s.skip(beginToken);
        c.stmtList = StatmList.parse(s);
        s.skip(endToken);

        leaveParser("compound statm");

        return c;
    }

    @Override
    public void check(Block curScope, Library lib) {
        stmtList.check(curScope, lib);
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrintLn("begin");
        Main.log.prettyIndent();

        stmtList.prettyPrint();

        Main.log.prettyOutdent();
        Main.log.prettyPrint("end");
    }

    @Override
    void genCode(CodeFile f) {
        stmtList.genCode(f);
    }
}

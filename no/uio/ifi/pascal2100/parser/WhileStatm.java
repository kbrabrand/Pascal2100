package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.whileToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.doToken;

import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.main.Main;

public class WhileStatm extends Statement {	
    Expression expr;
    Statement body;

    WhileStatm(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<while statm> on line " + lineNum;
    }

    public static WhileStatm parse(Scanner s) {
        enterParser("while statm");

        WhileStatm ws = new WhileStatm(s.curLineNum());

        s.skip(whileToken);
        ws.expr = Expression.parse(s);
        s.skip(doToken);
        ws.body = Statement.parse(s);

        leaveParser("while statm");

        return ws;
    }

    @Override
    public void check(Block curScope, Library lib) {
        expr.check(curScope, lib);
        body.check(curScope, lib);
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint("while ");
        expr.prettyPrint();

        if (body instanceof CompoundStatm) {
            Main.log.prettyPrint(" do ");
            body.prettyPrint();
        } else {
            Main.log.prettyPrintLn(" do");
            Main.log.prettyIndent();
            body.prettyPrint();
            Main.log.prettyOutdent();
        }
    }

}

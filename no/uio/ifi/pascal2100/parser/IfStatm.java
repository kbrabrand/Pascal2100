package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.ifToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.thenToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.elseToken;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class IfStatm extends Statement {
    public Expression expr;
    public Statement thenStatm;
    public Statement elseStatm = null;

    IfStatm(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<if statm> " + this.getSourceLocation();
    }

    public static IfStatm parse(Scanner s) {
        enterParser("if statm");

        IfStatm is = new IfStatm(s.curLineNum());

        s.skip(ifToken);
        is.expr = Expression.parse(s);
        s.skip(thenToken);
        is.thenStatm = Statement.parse(s);

        // Get the else part if the current token is an else token
        if (s.curToken.kind == elseToken) {
            s.skip(elseToken);
            is.elseStatm = Statement.parse(s);
        }

        leaveParser("if statm");

        return is;
    }

    @Override
    public void check(Block curScope, Library lib, Expression e) {
        expr.check(curScope, lib, e);
        thenStatm.check(curScope, lib, e);

        if (elseStatm != null) {
            elseStatm.check(curScope, lib, e);
        }
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint("if ");
        expr.prettyPrint();
        Main.log.prettyPrintLn(" then");
        Main.log.prettyIndent();
        thenStatm.prettyPrint();

        if (elseStatm != null) {
            Main.log.prettyOutdent();
            Main.log.prettyPrintLn();
            Main.log.prettyPrintLn("else");
            Main.log.prettyIndent();
            elseStatm.prettyPrint();
        }

        Main.log.prettyOutdent();
    }

    void genCode(CodeFile f) {
        String endLabel = f.getLocalLabel();
        String elseLabel;

        f.genInstr("", "", "", "Start if-statement");
        expr.genCode(f);
        f.genInstr("", "cmpl", "$0,%eax", "");

        if (elseStatm == null) {
            f.genInstr("", "je", endLabel, "");
            thenStatm.genCode(f);
        } else {
            elseLabel = f.getLocalLabel();

            f.genInstr("", "je", elseLabel, "");
            thenStatm.genCode(f);
            f.genInstr("", "jmp", endLabel, "");
            f.genInstr(elseLabel, "", "", "");
            elseStatm.genCode(f);
        }

        f.genInstr(endLabel, "", "", "End if-statement");
    }
}

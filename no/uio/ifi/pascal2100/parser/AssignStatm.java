package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.assignToken;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class AssignStatm extends Statement {
    public Variable var;
    public Expression expr;

    AssignStatm(String id, int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<assign statm> " + this.getSourceLocation();
    }

    public static AssignStatm parse(Scanner s) {
        enterParser("assign statm");

        AssignStatm as = new AssignStatm(s.curToken.id, s.curLineNum());

        as.var = Variable.parse(s);
        s.skip(assignToken);
        as.expr = Expression.parse(s);

        leaveParser("assign statm");

        return as;
    }

    @Override
    public void check(Block curScope, Library lib) {
        var.check(curScope, lib);
        expr.check(curScope, lib);
    }

    void prettyPrint() {
        var.prettyPrint();
        Main.log.prettyPrint(" := ");
        expr.prettyPrint();
    }

    public void genCode(CodeFile f) {
        expr.genCode(f);

        // Assign function return value
        if (var.nameDecl instanceof FuncDecl) {
            f.genInstr("", "movl", "%eax,-32(%ebp)");
            return;
        }

        // Simple variable assignment
        f.genInstr("", "movl", "-" + (4 * var.nameDecl.declLevel) + "(%ebp),%edx");
        f.genInstr("", "movl", "%eax," + var.nameDecl.declOffset + "(%edx)", var.name + " :=");
    }
}

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

        String varLocation = "-" + (4 * var.nameDecl.declLevel) + "(%ebp),%edx";
        
        // Assignment to array value
        if (var.nameDecl.type instanceof ArrayType) {
            ArrayType at = (ArrayType) var.nameDecl.type;

            var.expr.genCode(f);
            f.genInstr("", "pushl", "%eax");
            expr.genCode(f);
            f.genInstr("", "subl", at.getLow() + "%eax");
            f.genInstr("", "movl", varLocation);

            // get array offset value and lower end of range
            f.genInstr("", "leal", "-" + var.nameDecl.declOffset + "(%edx),%edx");
            f.genInstr("", "popl", "%ecx");
            f.genInstr("", "movl", "%ecx,(%edx,%eax,4)");
            return;
        }

        // Assign function return value
        if (var.nameDecl instanceof FuncDecl) {
            f.genInstr("", "movl", "%eax,-32(%ebp)");
            return;
        }

        // Simple variable assignment
        f.genInstr("", "movl", varLocation);
        f.genInstr("", "movl", "%eax,-0(%edx)");
    }
}

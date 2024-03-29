package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.commaToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class FuncCall extends Factor {
    public String name;
    public LinkedList<Expression> exprs = new LinkedList<Expression>();
    public FuncDecl decl;

    FuncCall(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<func call> " + this.getSourceLocation();
    }

    public static FuncCall parse(Scanner s) {
        enterParser("func call");

        s.test(nameToken);

        FuncCall fc = new FuncCall(s.curToken.id, s.curLineNum());

        s.readNextToken();

        if (s.curToken.kind == leftParToken) {
            s.skip(leftParToken);

            while (true) {
                fc.exprs.add(Expression.parse(s));

                if (s.curToken.kind != commaToken) {
                    break;
                }

                s.skip(commaToken);
            }

            s.skip(rightParToken);
        }

        leaveParser("func call");

        return fc;
    }

    @Override
    public void check(Block curScope, Library lib, Expression expr) {
        decl = (FuncDecl) curScope.findDecl(name, this);

        for (Expression e : exprs) {
            e.check(curScope, lib, expr);
        }

        decl.check(curScope, lib, expr);
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(name);

        if (exprs.size() > 0) {
            Main.log.prettyPrint("(");

            int i = 0;
            for (Expression e : exprs) {
                if (i++ > 0) {
                    Main.log.prettyPrint(", ");
                }

                e.prettyPrint();
            }

            Main.log.prettyPrint(")");
        }
    }

    public void genCode(CodeFile f) {
        for (int i = exprs.size() - 1; i >= 0; i--) {
            exprs.get(i).genCode(f);
            f.genInstr("", "pushl", "%eax", "Push param #" + (i + 1) + ".");
        }

        f.genInstr("", "call", decl.label);

        if (exprs.size() > 0) {
            f.genInstr(
                "",
                "addl", 
                "$" + exprs.size() * 4 + ",%esp", 
                "Pop parameter" + (exprs.size() > 1 ? "s" : "") + "."
            );
        }
    }
}

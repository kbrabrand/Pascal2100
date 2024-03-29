package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftBracketToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightBracketToken;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class Variable extends Factor {
    public String name;
    public Expression expr = null;
    public PascalDecl nameDecl = null;

    Variable(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<variable> " + this.getSourceLocation();
    }

    public static Variable parse(Scanner s) {
        enterParser("variable");

        s.test(nameToken);

        Variable v = new Variable(s.curToken.id, s.curLineNum());

        s.readNextToken();
        
        if (s.curToken.kind == leftBracketToken) {
            s.skip(leftBracketToken);

            v.expr = Expression.parse(s);

            s.skip(rightBracketToken);
        }

        leaveParser("variable");

        return v;
    }

    @Override
    public void check(Block curScope, Library lib, Expression e) {
        nameDecl = curScope.findDecl(name, this);
        nameDecl.check(curScope, lib, e);

        if (expr == null) {
            return;
        }

        expr.check(curScope, lib, e);
    }

    @Override
    void prettyPrint() {
        Main.log.prettyPrint(name);

        if (expr != null) {
            Main.log.prettyPrint("[");
            expr.prettyPrint();
            Main.log.prettyPrint("]");
        }
    }

    @Override
    void genCode(CodeFile f) {
        EnumLiteral el;

        // Check if this is a reference to a enumerated type value
        if (nameDecl instanceof EnumLiteral) {
            nameDecl.genCode(f);
            return;
        }

        // Check if this is a reference to a constant
        if (nameDecl instanceof ConstDecl) {
            ((ConstDecl) nameDecl).constant.genCode(f);
            return;
        }

        f.genInstr("", "movl", "-" + (4 * nameDecl.declLevel) + "(%ebp),%edx");
        f.genInstr("", "movl", nameDecl.declOffset + "(%edx),%eax", "  " + name);
    }
}

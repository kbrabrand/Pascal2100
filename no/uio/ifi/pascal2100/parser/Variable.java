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
    public void check(Block curScope, Library lib) {
        nameDecl = curScope.findDecl(name, this);

        if (expr != null) {
            expr.check(curScope, lib);
        }
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
        TypeDecl td;
        EnumType et;

        // Check if this is a reference to a enumerated type value
        if (nameDecl instanceof TypeDecl) {
            td = (TypeDecl) nameDecl;
            et = (EnumType) td.type;

            for (int i = 0; i < et.literals.size(); i++) {
                if (name.equals(et.literals.get(i).name)) {
                    f.genInstr("", "movl", "$" + i, "enum value " + name + "(=" + i + ")");
                    return;
                }
            }

            return;
        }

        f.genInstr("", "movl", "-" + (4 * nameDecl.declLevel) + "(%ebp),%edx");
        f.genInstr("", "movl", "-" + (36 + (4 * nameDecl.declOffset)) + "(%edx),%eax", name + "(" + nameDecl.identify() + ")");
    }
}

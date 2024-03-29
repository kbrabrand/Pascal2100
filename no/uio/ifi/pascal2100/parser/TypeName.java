package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class TypeName extends Type {
    public String name;
    public PascalDecl decl = null;

    TypeName(String id, int lNum) {
        super(lNum);
        name = id;
    }

    @Override
    public String identify() {
        return "<type name> " + name + " " + this.getSourceLocation();
    }

    public static TypeName parse(Scanner s) {
        enterParser("type name");

        s.test(nameToken);

        TypeName tn = new TypeName(s.curToken.id, s.curLineNum());
        s.readNextToken();

        leaveParser("type name");

        return tn;
    }

    public void check(Block curScope, Library lib, Expression e) {
        decl = curScope.findDecl(name, this);

        if (decl != null) {
            decl.check(curScope, lib, e);
        }
    }

    void prettyPrint() {
        Main.log.prettyPrint(name);
    }
}

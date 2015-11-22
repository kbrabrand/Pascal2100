package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class TypeName extends Type {
    public String name;
    public PascalDecl decl;

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

    @Override
    public void check(Block curScope, Library lib) {
        decl = curScope.findDecl(name, this);
    }

    void prettyPrint() {
        Main.log.prettyPrint(name);
    }

    @Override
    public int getSize() {
        return 0;
    }
}

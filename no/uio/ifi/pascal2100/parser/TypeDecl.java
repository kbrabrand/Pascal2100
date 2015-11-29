package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.equalToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class TypeDecl extends PascalDecl {
    public TypeName name;
    public Type type;

    TypeDecl(int lNum) {
        super(null, lNum);
    }

    @Override
    public String identify() {
        return "<type decl> " + this.getSourceLocation();
    }

    public static TypeDecl parse(Scanner s) {
        enterParser("type decl");

        TypeDecl td = new TypeDecl(s.curLineNum());

        td.name = TypeName.parse(s); 
        s.skip(equalToken);
        td.type = Type.parse(s);
        s.skip(semicolonToken);

        leaveParser("type decl");

        return td;
    }

    public void check(Block curScope, Library lib, Expression e) {
        type.check(curScope, lib, e != null ? e : null);
    }

    @Override
    public void check(Block curScope, Library lib) {
        check(curScope, lib, null);
    }

    public void prettyPrint() {
        name.prettyPrint();
        Main.log.prettyPrint(" = ");
        type.prettyPrint();
        Main.log.prettyPrint(";");
    }
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.colonToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class ParamDecl extends PascalDecl {
    public TypeName typeName;

    ParamDecl(String id, int lNum) {
        super(id, lNum);
    }

    @Override
    public String identify() {
        return "<param decl> " + this.getSourceLocation();
    }

    public static ParamDecl parse(Scanner s) {
        enterParser("param decl");

        s.test(nameToken);
        ParamDecl pd = new ParamDecl(s.curToken.id, s.curLineNum());
        s.readNextToken();

        s.skip(colonToken);
        pd.typeName = TypeName.parse(s);

        leaveParser("param decl");

        return pd;
    }

    @Override
    public void check(Block curScope, Library lib, Expression e) {
        curScope.findDecl(typeName.name, this);
        typeName.check(curScope, lib, e != null ? e : null);
    }

    @Override
    public void check(Block curScope, Library lib) {
        check(curScope, lib, null);
    }

    public void prettyPrint() {
        Main.log.prettyPrint(name + ": ");
        typeName.prettyPrint();
    }
}

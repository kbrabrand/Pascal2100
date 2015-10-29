package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.colonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class VarDecl extends PascalDecl {
    public Type type;

    VarDecl(String id, int lNum) {
        super(id, lNum);
    }

    @Override
    public String identify() {
        return "<var decl> on line " + lineNum;
    }

    public static VarDecl parse(Scanner s) {
        enterParser("var decl");

        s.test(nameToken);
        VarDecl vd = new VarDecl(s.curToken.id, s.curLineNum());
        s.readNextToken();

        s.skip(colonToken);
        vd.type = Type.parse(s);
        s.skip(semicolonToken);

        leaveParser("var decl");

        return vd;
    }

    @Override
    public void check(Block curScope, Library lib) {
        type.check(curScope, lib);

        if (type instanceof TypeName) {
             curScope.findDecl(((TypeName) type).name, this);
        }
    }

    public void prettyPrint() {
        Main.log.prettyPrint(name + " : ");
        type.prettyPrint();
        Main.log.prettyPrint(";");
    }
}

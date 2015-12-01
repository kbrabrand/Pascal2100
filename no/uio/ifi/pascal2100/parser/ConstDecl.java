package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.equalToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class ConstDecl extends PascalDecl {
    public Constant constant;

    ConstDecl(String id, int lNum) {
        super(id, lNum);
    }

    @Override
    public String identify() {
        return "<const decl> " + this.getSourceLocation();
    }

    public static ConstDecl parse(Scanner s) {
        enterParser("const decl");

        s.test(nameToken);
        ConstDecl cd = new ConstDecl(s.curToken.id, s.curLineNum());
        s.readNextToken();

        s.skip(equalToken);
        cd.constant = Constant.parse(s);
        s.skip(semicolonToken);

        leaveParser("const decl");

        return cd;
    }

    @Override
    public void check(Block curScope, Library lib, Expression e) {
        constant.check(curScope, lib, e);
    }

    public void prettyPrint() {
        Main.log.prettyPrint(name + " = ");
        constant.prettyPrint();
        Main.log.prettyPrint(";");
    }
}

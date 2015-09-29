package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.equalToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import no.uio.ifi.pascal2100.scanner.TokenKind;

public class ConstDecl extends PascalSyntax {
    public NamedConst name;
    public Constant constant;

    ConstDecl(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<const-decl> on line " + lineNum;
    }

    public static ConstDecl parse(Scanner s) {
        enterParser("const-decl");

        ConstDecl cd = new ConstDecl(s.curLineNum());

        cd.name = NamedConst.parse(s); 
        s.skip(equalToken);
        cd.constant = Constant.parse(s);
        s.skip(semicolonToken);

        leaveParser("const-decl");

        return cd;
    }

    public void prettyPrint() {
        name.prettyPrint();
        Main.log.prettyPrint(" = ");
        constant.prettyPrint();
        Main.log.prettyPrint(";");
    }
}

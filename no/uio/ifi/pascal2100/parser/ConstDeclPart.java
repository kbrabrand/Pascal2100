package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.constToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class ConstDeclPart extends PascalSyntax {
    public LinkedList<ConstDecl> decls = new LinkedList<ConstDecl>();

    ConstDeclPart(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<const decl part> on line " + lineNum;
    }

    public static ConstDeclPart parse(Scanner s) {
        enterParser("const decl part");

        s.skip(constToken);

        ConstDeclPart cdp = new ConstDeclPart(s.curLineNum());

        do {
            cdp.decls.add(ConstDecl.parse(s));
        } while (s.curToken.kind == nameToken);

        leaveParser("const decl part");

        return cdp;
    }

    @Override
    public void check(Block curScope, Library lib) {
        for (ConstDecl cd : decls) {
            cd.check(curScope, lib);
        }
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn();
        Main.log.prettyPrint("const ");

        int i = 0;
        for (ConstDecl cd : decls) {
            if (i++ > 0) {
                Main.log.prettyPrint(" ");
            }

            cd.prettyPrint();
        }
    }
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.typeToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class TypeDeclPart extends PascalSyntax {
    public LinkedList<TypeDecl> decls = new LinkedList<TypeDecl>();

    TypeDeclPart(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<type decl part> on line " + lineNum;
    }

    public static TypeDeclPart parse(Scanner s) {
        enterParser("type decl part");

        s.skip(typeToken);

        TypeDeclPart tdp = new TypeDeclPart(s.curLineNum());

        do {
            tdp.decls.add(TypeDecl.parse(s));
        } while (s.curToken.kind == nameToken);

        leaveParser("type decl part");

        return tdp;
    }

    @Override
    public void check(Block curScope, Library lib) {
        for (TypeDecl td : decls) {
            td.check(curScope, lib);
        }
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn();
        Main.log.prettyPrint("type ");

        int i = 0;
        for (TypeDecl td : decls) {
            if (i++ > 0) {
                Main.log.prettyPrint(" ");
            }

            td.prettyPrint();
        }
    }
}

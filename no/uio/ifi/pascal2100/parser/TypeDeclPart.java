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
        return "<const-decl-part> on line " + lineNum;
    }

    public static TypeDeclPart parse(Scanner s) {
        enterParser("type-decl-part");

        s.skip(typeToken);

        TypeDeclPart tdp = new TypeDeclPart(s.curLineNum());

        do {
            tdp.decls.add(TypeDecl.parse(s));
        } while (s.curToken.kind == nameToken);

        leaveParser("type-decl-part");

        return tdp;
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn("type");
        
        for (TypeDecl td : decls) {
            Main.log.prettyPrint(" ");
            td.prettyPrint();
        }
    }
}

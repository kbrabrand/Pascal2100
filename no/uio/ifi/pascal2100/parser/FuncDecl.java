package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.functionToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.colonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class FuncDecl extends ProcDecl {
    public TypeName typeName;

    FuncDecl(String id, int lNum) {
        super(id, lNum);
    }

    @Override
    public String identify() {
        return "<func-decl> on line " + lineNum;
    }

    public static FuncDecl parse(Scanner s) {
        enterParser("func-decl");

        s.skip(functionToken);

        s.test(nameToken);
        FuncDecl fd = new FuncDecl(s.curToken.id, s.curLineNum());
        s.readNextToken();

        if (s.curToken.kind == leftParToken) {
            fd.paramDeclList = ParamDeclList.parse(s);
        }

        s.skip(colonToken);

        fd.typeName = TypeName.parse(s);

        s.skip(semicolonToken);

        fd.block = Block.parse(s);
        fd.block.context = fd;

        s.skip(semicolonToken);

        leaveParser("func-decl");

        return fd;
    }

    public void prettyPrint() {
        Main.log.prettyPrint("function " + name + " ");

        if (paramDeclList != null) {
            paramDeclList.prettyPrint();
        }

        Main.log.prettyPrint(":");
        typeName.prettyPrint();

        Main.log.prettyPrint("; ");
        block.prettyPrint();

        Main.log.prettyPrint(";");
    }
}

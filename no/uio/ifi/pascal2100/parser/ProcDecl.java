package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.procedureToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class ProcDecl extends PascalDecl {
    public ParamDeclList paramDeclList = null;
    public Block block;

    ProcDecl(String id, int lNum) {
        super(id, lNum);
    }

    @Override
    public String identify() {
        return "<proc-decl> on line " + lineNum;
    }

    public static ProcDecl parse(Scanner s) {
        enterParser("proc-decl");

        s.skip(procedureToken);

        s.test(nameToken);
        ProcDecl pd = new ProcDecl(s.curToken.id, s.curLineNum());
        s.readNextToken();

        if (s.curToken.kind == leftParToken) {
            pd.paramDeclList = ParamDeclList.parse(s);
        }

        s.skip(semicolonToken);

        pd.block = Block.parse(s);
        pd.block.context = pd;
        
        s.skip(semicolonToken);

        leaveParser("proc-decl");

        return pd;
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn();
        Main.log.prettyPrint("procedure " + name + " ");

        if (paramDeclList != null) {
            paramDeclList.prettyPrint();
        }

        Main.log.prettyPrint("; ");
        block.prettyPrint();
        Main.log.prettyPrintLn(";");
    }
}

package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.varToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class VarDeclPart extends PascalSyntax {
    public LinkedList<VarDecl> decls = new LinkedList<VarDecl>();

    VarDeclPart(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<var-decl-part> on line " + lineNum;
    }

    public static VarDeclPart parse(Scanner s) {
        enterParser("var-decl-part");

        s.skip(varToken);
        
        VarDeclPart vdp = new VarDeclPart(s.curLineNum());

        do {
            vdp.decls.add(VarDecl.parse(s));
        } while (s.curToken.kind == nameToken);

        leaveParser("var-decl-part");

        return vdp;
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn();
        Main.log.prettyPrint("var ");

        int i = 0;
        for (VarDecl vd : decls) {
            if (i++ > 0) {
                Main.log.prettyPrint(" ");
            }

            vd.prettyPrint();
        }
    }
}

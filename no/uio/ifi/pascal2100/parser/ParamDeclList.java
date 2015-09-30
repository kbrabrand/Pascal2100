package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rightParToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;

import java.util.LinkedList;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class ParamDeclList extends PascalSyntax {
    public LinkedList<ParamDecl> decls = new LinkedList<ParamDecl>();

    ParamDeclList(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<param-decl-list> on line " + lineNum;
    }

    public static ParamDeclList parse(Scanner s) {
        enterParser("param-decl-list");

        s.skip(leftParToken);
        
        ParamDeclList pdl = new ParamDeclList(s.curLineNum());

        while(true) {
            pdl.decls.add(ParamDecl.parse(s));

            if (s.curToken.kind != semicolonToken) {
                break;
            }
            
            s.skip(semicolonToken);
        }

        s.skip(rightParToken);
        
        leaveParser("param-decl-list");

        return pdl;
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn("(");
        
        int i = 0;
        for (ParamDecl pd : decls) {
            if (i++ > 0) {
                Main.log.prettyPrint("; ");
            }
            
            pd.prettyPrint();
        }
        
        Main.log.prettyPrintLn(")");
    }
}

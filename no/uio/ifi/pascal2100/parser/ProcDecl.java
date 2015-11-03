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
        return "<proc decl> " + this.getSourceLocation();
    }

    public static ProcDecl parse(Scanner s) {
        enterParser("proc decl");

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

        leaveParser("proc decl");

        return pd;
    }

    @Override
    public void check(Block curScope, Library lib) {
        paramDeclList.check(curScope, lib);
        curScope.addDecl(name, this);

        for (ParamDecl pd: paramDeclList.decls) {
            block.addDecl(pd.name, pd);
        }

        block.check(curScope, block, lib);
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn();
        Main.log.prettyPrint("procedure " + name);

        if (paramDeclList != null) {
            Main.log.prettyPrint(" ");
            paramDeclList.prettyPrint();
        }

        Main.log.prettyPrint("; ");
        block.prettyPrint();
        Main.log.prettyPrintLn("; {" + name + "}");
    }
}

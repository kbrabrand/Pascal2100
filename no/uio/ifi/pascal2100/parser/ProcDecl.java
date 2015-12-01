package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.procedureToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class ProcDecl extends PascalDecl {
    public ParamDeclList paramDeclList = null;
    public Block block;
    public String label;

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
    public void check(Block curScope, Library lib, Expression e) {
        ParamDecl pd;

        curScope.addDecl(name, this);

        if (paramDeclList != null) {
            for (int i = 0; i < paramDeclList.decls.size(); i++) {
                pd = paramDeclList.decls.get(i);

                pd.declLevel = curScope.blockLevel + 1;
                pd.declOffset = 8 + (4 * i);

                block.addDecl(pd.name, pd);
            }

            paramDeclList.check(curScope, lib, e);
        }

        block.check(curScope, block, lib, e);
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

    @Override
    public void genCode(CodeFile f) {
        label = f.getLabel("proc$" + name);

        block.genDeclCode(f);

        f.genInstr(label, "");
        f.genInstr("", "enter", "$" + block.getSize() + "," + block.blockLevel, "Start of " + name);
        block.genCode(f);
        f.genInstr("", "leave", "", "End of " + name);
        f.genInstr("", "ret");
    }
}

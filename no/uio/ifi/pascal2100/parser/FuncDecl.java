package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.nameToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.functionToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.colonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.semicolonToken;
import static no.uio.ifi.pascal2100.scanner.TokenKind.leftParToken;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;

public class FuncDecl extends ProcDecl {
    public TypeName typeName;

    FuncDecl(String id, int lNum) {
        super(id, lNum);
    }

    @Override
    public String identify() {
        return "<func decl> " + this.getSourceLocation();
    }

    public static FuncDecl parse(Scanner s) {
        enterParser("func decl");

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

        leaveParser("func decl");

        return fd;
    }

    public void check(Block curScope, Library lib, Expression e) {
        TypeDecl td = (TypeDecl) curScope.findDecl(typeName.name, this);
        td.check(curScope, lib, e);

        super.check(curScope, lib, null);
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn();
        Main.log.prettyPrint("function " + name);

        if (paramDeclList != null) {
            Main.log.prettyPrint(" ");
            paramDeclList.prettyPrint();
        }

        Main.log.prettyPrint(": ");
        typeName.prettyPrint();

        Main.log.prettyPrint("; ");
        block.prettyPrint();

        Main.log.prettyPrintLn("; {" + name + "}");
    }

    @Override
    public void genCode(CodeFile f) {
        label = f.getLabel("func$" + name);

        for (PascalDecl pd : paramDeclList.decls) {
            pd.genCode(f);
        }

        f.genInstr(label, "");
        f.genInstr("", "enter", "$" + block.getSize() + "," + block.blockLevel, "Start of " + name);
        block.genCode(f);
        f.genInstr("", "movl", "-32(%ebp),%eax", "Fetch return value");
        f.genInstr("", "leave", "", "End of " + name);
        f.genInstr("", "ret");
    }
}

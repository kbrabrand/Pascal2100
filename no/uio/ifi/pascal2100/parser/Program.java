package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.CodeFile;
import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

/* <program> ::= 'program' <name> ';' <block> '.' */

public class Program extends PascalDecl {
    public Block progBlock;
    public PascalDecl context;
    public String name;

    Program(String id, int num) {
        super(id, num);
        name = id;
    }

    @Override
    public String identify() {
        return "<program> " + name + " " + this.getSourceLocation();
    }

    public static Program parse(Scanner s) {
        enterParser("program");

        s.skip(programToken);
        s.test(nameToken);

        Program p = new Program(s.curToken.id, s.curLineNum());

        s.readNextToken();
        s.skip(semicolonToken);

        p.progBlock = Block.parse(s);
        p.progBlock.context = p;

        s.skip(dotToken);

        leaveParser("program");

        return p;
    }

    @Override
    public void check(Block curScope, Library lib) {
        progBlock.check(curScope, progBlock, lib);
    }

    public void prettyPrint() {
        Main.log.prettyPrintLn("program " + name + ";");
        progBlock.prettyPrint();
        Main.log.prettyPrintLn(".");
    }

    @Override
    void genCode(CodeFile f) {
        int level = 1;
        String progLabel = "func$" + name + "_" + level;
        int progBlockSize = progBlock.decls.size() * 4;

        f.genInstr("", ".extern", "write_char");
        f.genInstr("", ".extern", "write_int");
        f.genInstr("", ".extern", "write_string");
        f.genInstr("", ".globl", "_main");
        f.genInstr("", ".globl", "main");

        f.genInstr("_main", "", "");
        f.genInstr("main", "call", progLabel, "Start program");
        f.genInstr("", "movl", "$0,%eax", "Set status 0");
        f.genInstr("", "ret", "", "terminate the program");

        f.genInstr(progLabel, "enter", "$" + (32 + progBlockSize) + ",$" + level, "Start of " + name);
        progBlock.genCode(f);
        f.genInstr("", "leave");
        f.genInstr("", "ret");
    }
}

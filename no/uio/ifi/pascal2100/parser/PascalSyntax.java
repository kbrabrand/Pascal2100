package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.*;

public abstract class PascalSyntax {
    public int lineNum;

    PascalSyntax(int n) {
        lineNum = n;
    }

    boolean isInLibrary() {
        return lineNum < 0;
    }

    String getSourceLocation() {
        if (this.isInLibrary()) {
            return "in the library";
        } else {
            return "on line " + lineNum;
        }
    }

    abstract void check(Block curScope, Library lib);

    abstract void genCode(CodeFile f);
    abstract public String identify();
    abstract void prettyPrint();

    void error(String message) {
        Main.error("Error at line " + lineNum + ": " + message);
    }

    static void enterParser(String nonTerm) {
        Main.log.enterParser(nonTerm);
    }

    static void leaveParser(String nonTerm) {
        Main.log.leaveParser(nonTerm);
    }
}

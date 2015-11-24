package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.main.Main;
import no.uio.ifi.pascal2100.scanner.Scanner;
import static no.uio.ifi.pascal2100.scanner.TokenKind.rangeToken;

public class RangeType extends Type {
    public Constant from;
    public Constant to;

    RangeType(int lNum) {
        super(lNum);
    }

    @Override
    public String identify() {
        return "<range type> " + this.getSourceLocation();
    }

    public static RangeType parse(Scanner s) {
        enterParser("range type");

        RangeType rt = new RangeType(s.curLineNum());

        rt.from = Constant.parse(s);

        s.skip(rangeToken);

        rt.to = Constant.parse(s);

        leaveParser("range type");

        return rt;
    }

    @Override
    public void check(Block curScope, Library lib) {
        from.check(curScope, lib);
        to.check(curScope, lib);
    }

    void prettyPrint() {
        from.prettyPrint();
        Main.log.prettyPrint(" .. ");
        to.prettyPrint();
    }
}

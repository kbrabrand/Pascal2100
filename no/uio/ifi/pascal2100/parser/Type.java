package no.uio.ifi.pascal2100.parser;

import no.uio.ifi.pascal2100.scanner.Scanner;

abstract class Type extends PascalSyntax {
    Type(int lNum) {
        super(lNum);
    }
    
    public static Type parse(Scanner s) {
        enterParser("type");

        Type t;
        
        switch (s.nextToken.kind) {
            case rangeToken:
                t = RangeType.parse(s);
                break;
            default:
                switch (s.curToken.kind) {
                    case nameToken:
                        t = TypeName.parse(s);
                        break;
                    case leftParToken:
                        t = EnumType.parse(s);
                        break;
                    default:
                        t = ArrayType.parse(s);
                        break;
                }
                break;
        }

        leaveParser("type");

        return t;
    }

    abstract public int getSize();
}

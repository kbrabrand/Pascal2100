package no.uio.ifi.pascal2100.parser;

import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

import no.uio.ifi.pascal2100.scanner.Token;

public abstract class Operator extends PascalSyntax {
    Operator(int lNum) {
        super(lNum);
    }
    
    public static boolean checkWhetherPrefixOperator(Token t) {
        return (t.kind == addToken || t.kind == subtractToken);
    }
    
    public static boolean checkWhetherRelOperator(Token t) {
        return (
            t.kind == equalToken ||
            t.kind == notEqualToken ||
            t.kind == lessToken||
            t.kind == lessEqualToken ||
            t.kind == greaterToken||
            t.kind == greaterEqualToken
        );
    }
    
    public static boolean checkWhetherTermOperator(Token t) {
        return (
            t.kind == addToken ||
            t.kind == subtractToken ||
            t.kind == orToken
        );
    }
    
    public static boolean checkWhetherFactorOperator(Token t) {
        return (
            t.kind == multiplyToken ||
            t.kind == divToken ||
            t.kind == modToken ||
            t.kind == andToken
        );
    }
}

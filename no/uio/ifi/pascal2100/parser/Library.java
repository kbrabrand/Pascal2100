package no.uio.ifi.pascal2100.parser;

public class Library extends Block {
    public Library() {
        super(-1);

        addEof();
        addChar();
        addBoolean();
        addInteger();
        addWrite();
    }

    void addEof() {
        ConstDecl eol = new ConstDecl("eol", -1);
        eol.constant = new CharLiteral('\n', -1);

        addDecl(eol.name, eol);
    }

    void addChar() {
        RangeType charRange = new RangeType(-1);
        charRange.from = new CharLiteral('␀', -1); // NUL
        charRange.to = new CharLiteral('␡', -1); // DEL

        TypeDecl charType = new TypeDecl(-1);
        charType.name = new TypeName("char", -1);
        charType.type = charRange;

        addDecl(charType.name.name, charType);
    }

    void addBoolean() {
        EnumType enumType = new EnumType(-1);
        enumType.literals.add(new EnumLiteral("true", -1));
        enumType.literals.add(new EnumLiteral("false", -1));

        TypeDecl boolType = new TypeDecl(-1);
        boolType.name = new TypeName("Boolean", -1);
        boolType.type = enumType; 

        addDecl(boolType.name.name, boolType);
    }

    void addInteger() {
        RangeType integer = new RangeType(-1);
        integer.from = new NumberLiteral(-2147483648, -1);
        integer.to = new NumberLiteral(2147483647, -1);

        TypeDecl integerType = new TypeDecl(-1);
        integerType.name = new TypeName("Integer", -1);
        integerType.type = integer;

        addDecl(integerType.name.name, integerType);
    }

    void addWrite() {
        addDecl("write", new ProcDecl("write", -1));
    }
}

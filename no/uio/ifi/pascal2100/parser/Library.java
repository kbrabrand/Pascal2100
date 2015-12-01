package no.uio.ifi.pascal2100.parser;

public class Library extends Block {
    public Library() {
        super(-1);

        typeDeclPart = new TypeDeclPart(lineNum);
        constDeclPart = new ConstDeclPart(lineNum);
        stmtList = new StatmList(lineNum);

        addEol();
        addChar();
        addBoolean();
        addInteger();
        addWrite();
    }

    void addEol() {
        ConstDecl eol = new ConstDecl("eol", lineNum);
        eol.constant = new CharLiteral('\n', lineNum);

        constDeclPart.decls.add(eol);
    }

    void addChar() {
        RangeType charRange = new RangeType(lineNum);
        charRange.from = new CharLiteral('␀', lineNum); // NUL
        charRange.to = new CharLiteral('␡', lineNum); // DEL

        TypeDecl charType = new TypeDecl(lineNum);
        charType.name = new TypeName("char", lineNum);
        charType.type = charRange;

        typeDeclPart.decls.add(charType);
    }

    void addBoolean() {
        EnumType enumType = new EnumType(lineNum);
        enumType.literals.add(new EnumLiteral("false", lineNum));
        enumType.literals.add(new EnumLiteral("true", lineNum));

        TypeDecl boolType = new TypeDecl(lineNum);
        boolType.name = new TypeName("Boolean", lineNum);
        boolType.type = enumType; 

        typeDeclPart.decls.add(boolType);
    }

    void addInteger() {
        RangeType integer = new RangeType(lineNum);
        integer.from = new NumberLiteral(-2147483648, lineNum);
        integer.to = new NumberLiteral(2147483647, lineNum);

        TypeDecl integerType = new TypeDecl(lineNum);
        integerType.name = new TypeName("Integer", lineNum);
        integerType.type = integer;

        typeDeclPart.decls.add(integerType);
    }

    void addWrite() {
        StatmList sl = new StatmList(lineNum);

        Block b = new Block(lineNum);
        b.stmtList = sl;

        ProcDecl write = new ProcDecl("write", lineNum);
        write.block = b;

        procDeclList.add(write);
    }
}

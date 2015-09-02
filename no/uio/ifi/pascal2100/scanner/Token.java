package no.uio.ifi.pascal2100.scanner;

import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

public class Token {
    public TokenKind kind;
    public String id, strVal;
    public int intVal, lineNum;

    Token(TokenKind k, int lNum) {
	kind = k;  lineNum = lNum;
    }

    Token(String s, int lNum) {
	if (s.equals("and"))
	    kind = andToken;
	else if (s.equals("array"))
	    kind = arrayToken;
	else if (s.equals("begin"))
	    kind = beginToken;
	else if (s.equals("const"))
	    kind = constToken;
	else if (s.equals("div"))
	    kind = divToken;
	else if (s.equals("do"))
	    kind = doToken;
	else if (s.equals("else"))
	    kind = elseToken;
	else if (s.equals("end"))
	    kind = endToken;
	else if (s.equals("function"))
	    kind = functionToken;
	else if (s.equals("if"))
	    kind = ifToken;
	else if (s.equals("mod"))
	    kind = modToken;
	else if (s.equals("not"))
	    kind = notToken;
	else if (s.equals("of"))
	    kind = ofToken;
	else if (s.equals("or"))
	    kind = orToken;
	else if (s.equals("procedure"))
	    kind = procedureToken;
	else if (s.equals("program"))
	    kind = programToken;
	else if (s.equals("then"))
	    kind = thenToken;
	else if (s.equals("type"))
	    kind = typeToken;
	else if (s.equals("var"))
	    kind = varToken;
	else if (s.equals("while"))
	    kind = whileToken;
	else
	    kind = nameToken;

	id = s;  lineNum = lNum;
    }

    Token(String any, String s, int lNum) {
	kind = stringValToken;  strVal = s;  lineNum = lNum;
    }

    Token(int n, int lNum) {
	kind = intValToken;  intVal = n;  lineNum = lNum;
    }


    public String identify() {
	String t = kind.identify();
	if (lineNum > 0) 
	    t += " on line " + lineNum;

	switch (kind) {
	case nameToken:      t += ": " + id;  break;
	case intValToken:    t += ": " + intVal;  break;
	case stringValToken: t += ": '" + strVal + "'";  break;
	}
	return t;
    }
}

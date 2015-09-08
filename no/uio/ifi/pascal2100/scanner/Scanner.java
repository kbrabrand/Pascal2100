package no.uio.ifi.pascal2100.scanner;

import no.uio.ifi.pascal2100.main.*;
import static no.uio.ifi.pascal2100.scanner.TokenKind.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;

public class Scanner {
    public Token curToken = null, nextToken = null;

    private LineNumberReader sourceFile = null;
    private String sourceFileName, sourceLine = "";
    private int sourcePos = 0;

    // Regular expression for matching one-line comment
    private static Pattern oneLineCommentRegexp = Pattern.compile("^(?:\\/\\*(.*)\\*\\/|\\{.*\\})");
    
    // Regular expression for finding start of comment over multiple lines
    private static Pattern multilineCommentStartRegexp = Pattern.compile("^\\{|\\/\\*");
    
    private static Pattern nameRegexp = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*");
    
    private static Pattern numericLiteralRegexp = Pattern.compile("^\\d+");
    
    private static Pattern stringLiteralRegexp = Pattern.compile("^'((?:\\.|[^\\'])*)'");

    public Scanner(String fileName) {
        sourceFileName = fileName;

        try {
            sourceFile = new LineNumberReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            Main.error("Cannot read " + fileName + "!");
        }

        // Read the first two tokens
        readNextToken();
        readNextToken();
    }

    public String identify() {
        return "Scanner reading " + sourceFileName;
    }

    public int curLineNum() {
        return getFileLineNum();
    }

    private void error(String message) {
        Main.error("Scanner error on line " + curLineNum() + ": " + message);
    }

    public void readNextToken() {
    	Token tok;
    	
    	// Note end of token marker if we're at the end of the file
        if (sourceFile == null) {
        	// Create end of file token
        	tok = new Token(eofToken, getFileLineNum());
        	nextToken = tok;
        	
        	// Note the token
            Main.log.noteToken(tok);
        	
            return;
        }
	    
        // Skip non-tokens
        skipNonTokens();
        
        boolean ok = 
        	testPrefix("+", addToken) || 
            testPrefix(":=", assignToken) ||
            testPrefix(":", colonToken) ||
            testPrefix(",", commaToken) ||
            testPrefix(".", dotToken) ||
            testPrefix("=", equalToken) ||
            testPrefix("<", lessToken) || testPrefix(">", greaterToken) ||
            testPrefix("<=", lessEqualToken) || testPrefix(">=", greaterEqualToken) ||
            testPrefix("[", leftBracketToken) || testPrefix("]", rightBracketToken) ||
            testPrefix("(", leftParToken) || testPrefix(")", rightParToken) ||
            testPrefix("*", multiplyToken) ||
            testPrefix("<>", notEqualToken) ||
            testPrefix("..", rangeToken) ||
            testPrefix(";", semicolonToken) ||
            testPrefix("-", subtractToken) ||

            testPrefix("and", andToken) ||
            testPrefix("array", arrayToken) ||
            testPrefix("begin", beginToken) ||
            testPrefix("const", constToken) ||
            testPrefix("div", divToken) ||
		    testPrefix("do", doToken) ||
		    testPrefix("else", elseToken) ||
		    testPrefix("end", endToken) ||
		    testPrefix("function", functionToken) ||
		    testPrefix("if", ifToken) ||
		    testPrefix("mod", modToken) ||
		    testPrefix("not", notToken) ||
		    testPrefix("of", ofToken) ||
		    testPrefix("or", orToken) ||
		    testPrefix("procedure", procedureToken) ||
		    testPrefix("program", programToken) ||
		    testPrefix("then", thenToken) ||
		    testPrefix("type", typeToken) ||
		    testPrefix("var", varToken) ||
		    testPrefix("while", whileToken) ||
		    
		    testRegexp(stringLiteralRegexp, stringValToken) ||
		    testRegexp(numericLiteralRegexp, intValToken) ||
		    testRegexp(nameRegexp, nameToken);
	    
        if (!ok) {
        	error(
        		"Tokenization failed at line " + getFileLineNum() + ", col " + sourcePos + ":\n" +
        		sourceLine + "\n" +
        		new String(new char[sourcePos]).replace('\0', ' ') + "^"
        	);
        }
        
        skipWhitespace();
        
        // Read next line if we're at the end of the current one
        if (getSourceLineRemainderLength() == 0) {
        	readNextLine();
        }
    }

    private void readNextLine() {
        if (sourceFile != null) {
            try {
                sourceLine = sourceFile.readLine();

                if (sourceLine == null) {
                    sourceFile.close();

                    sourceFile = null;
                    sourceLine = "";
                } else {
                    sourceLine += " ";
                }

                sourcePos = 0;
            } catch (IOException e) {
                Main.error("Scanner error: unspecified I/O error!");
            }
        }
        if (sourceFile != null) {
            Main.log.noteSourceLine(getFileLineNum(), sourceLine);
        }
    }

    private int getFileLineNum() {
        return (sourceFile!=null ? sourceFile.getLineNumber() : 0);
    }
    
    
    /**
	 * Update curToken and nextToken after finding a new token.
     * 
     * @param Token tok The discovered token
     */
    private void setToken(Token tok) {
    	if (curToken == null) {
        	curToken = tok;
        } else if (curToken != null && nextToken == null) {
        	nextToken = tok;
        } else {
        	curToken = nextToken;
        	nextToken = tok;
        }
    }

    // Source test utilities:

	/**
     * Test if the remainder of the sourceLine starts with a given prefix
     *
     * @param String prefix The prefix to test
     * @param TokenKind tokenKind The TokenKind that corresponds to the given prefix
	 * @return boolean Whether or not the prefix was found
     */
    private boolean testPrefix(String prefix, TokenKind tokenKind) {
		if (!getSourceLineRemainder().startsWith(prefix)) {
			return false;
		}
		
		// "Consume" the part of the input that was matched
        consumeSource(prefix.length());
	        
        // Create token from matched string
        Token tok = new Token(tokenKind, getFileLineNum());
        
        Main.log.noteToken(tok);
        setToken(tok);
        
        return true;
    }

    /**
     * Test a regular expression against the sourceLine
     * 
     * @param Pattern pattern The pattern to match
     * @param TokenKind the type of token to note if we have a match
     * @return boolean Whether or not the pattern match
     */
    private boolean testRegexp(Pattern pattern, TokenKind tokenKind) {
    	// Try the pattern against the remainder of the source line
        Matcher matcher = pattern.matcher(getSourceLineRemainder());
        
        // No match
        if (!matcher.lookingAt()) {
        	return false;
        }
    	
        // "Consume" the part of the input that was matched
        consumeSource(matcher.end());
	    
        Token tok;
        
        // Create token from matched string
        switch (tokenKind) {
	        case stringValToken:
	    		tok = new Token("", matcher.group(), getFileLineNum());
	    		break;
        	case nameToken:
        		tok = new Token(matcher.group().toLowerCase(), getFileLineNum());
        		break;
        	case intValToken:
        		tok = new Token(Integer.parseInt(matcher.group()), getFileLineNum());
        		break;
        	default:
        		tok = new Token(tokenKind, getFileLineNum());
        		break;
        }
        
        Main.log.noteToken(tok);
        setToken(tok);
        
        return true;
    }
    
    /**
     * Consume the source by cutting off a given number of characters from the start of
     * the sourceRemainder string. If the string is emptied, a new line is read.
     *
     * @param int n Number of characters to cut off from the start
     */
    private void consumeSource(int n) {
        int remainingChars = getSourceLineRemainder().length();
        
    	if (remainingChars < n) {
            Main.error(curLineNum(),
                "Trying to consume " + n +
                " characters from sourceLine, but only " +
                remainingChars + " characters remain."
            );
        }

        // Cut off characters from the start of the remainder
        sourcePos += n;
    }

    /**
     * Skips whitespace characters from the start of the current line
     */
    private void skipWhitespace() {
        String remainder = getSourceLineRemainder();
        int remainderLength = remainder.length();
        
    	int i = 0;

        while (i < remainderLength && Character.isWhitespace(remainder.charAt(i))) {
            i++;
        }

        consumeSource(i);
    }

    /**
     * Skip comments from the start of the current line
     */
    private void skipComments() {
    	// Check if this is a one line comment
    	Matcher commentMatcher = oneLineCommentRegexp.matcher(getSourceLineRemainder());
    	
    	if (commentMatcher.lookingAt()) {
    		consumeSource(commentMatcher.end());
    		
    		readNextLine();
    		return;
    	}
    	
    	// Check if this is the start of a comment over multiple lines
    	Matcher multilineCommentMatcher = multilineCommentStartRegexp.matcher(getSourceLineRemainder());
    	
    	if (multilineCommentMatcher.lookingAt()) {
    		// Prepare the termination pattern based on what opening symbol we found 
    		Pattern commentTerminationRegexp = 
				multilineCommentMatcher.group() == "{" ? 
				Pattern.compile(".*\\}") :
				Pattern.compile(".*\\*\\/");
    		
    		readNextLine();
    		
    		boolean foundCommentTerminationSymbol = false;
    		
    		// Get lines for as long as it takes to find the termination marker 
    		do {
    			readNextLine();
    			Matcher multilineEndMatcher = commentTerminationRegexp.matcher(getSourceLineRemainder());
    			
    			// Check if we found the end
    			foundCommentTerminationSymbol = multilineEndMatcher.lookingAt();
    		} while (!foundCommentTerminationSymbol);
    		
    		// We found the termination symbol, read the next line so we're ready
    		if (foundCommentTerminationSymbol) {
    			readNextLine();
    		}
    	}
    }
    
    /**
     * Skip empty lines
     * @return 
     */
    private void skipEmptyLines() {
    	while (getSourceLineRemainderLength() == 0) {
    		readNextLine();
    	}
    }
    
    /**
     * Skip non-tokens, like whitespace, comments and empty newlines
     */
    private void skipNonTokens() {
    	skipEmptyLines();
        skipWhitespace();
        skipComments();

        // If we get here and the line is empty, repeat to check for more stuff to skip
        if (getSourceLineRemainderLength() == 0) {
        	skipNonTokens();
        }
    }
    
    /**
     * Get the remaining part of the sourceLine based on the current sourcePos offset
     * 
     * @return String The remainder of the source line
     */
    private String getSourceLineRemainder() {
    	return sourceLine.substring(sourcePos);
    }
    
    /**
     * Get the number of characters left on the current line based on the sourcePos offset
     * 
     * @return integer Numbers of characters left on the source line
     */
    private int getSourceLineRemainderLength() {
    	return getSourceLineRemainder().length();
    }
    
    
    // Parser tests:

    public void test(TokenKind t) {
        if (curToken.kind != t) {
            testError(t.toString());
        }
    }

    public void testError(String message) {
        Main.error(curLineNum(),
            "Expected a " + message +
            " but found a " + curToken.kind + "!");
    }

    public void skip(TokenKind t) {
        test(t);
        readNextToken();
    }
}

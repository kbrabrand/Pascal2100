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
    
    // Match from the start of the string, using a non-capturing group so we 
    // don't have to repeat the leading ^ for each of the alternatives
    private static Pattern rTokens = Pattern.compile(
    	"^(?:" + 
    	"\\(|\\)|\\{|\\}|;|,|\\+|-|\\*|\\/|%|!|:=|:|==|<>|<=|>=|<|>|" + // Character sequence with a special meaning 
    	"\\d+" + // Numeric literal
    	"|true|false|" + // Boolean
    	"if|then|else|while|do|" + // Keywords
    	"[a-zA-Z_][a-zA-Z0-9_]*|" + // Identifier
    	"'((?:\\.|[^\\'])*)'" + // String literal
    	")"
    );

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
        
        // Try the pattern against the remainder of the source line
        Matcher matcher = rTokens.matcher(sourceLine.substring(sourcePos));
	        
        // No match
        if (!matcher.lookingAt()) {
        	error("Tokenizing failed");
        }
	        
        // "Consume" the part of the input that was matched
        consumeSource(matcher.end());
	        
        // Create token from matched string
        tok = new Token(matcher.group(), getFileLineNum());
	        
        skipWhitespace();
        
        // Note the token
        Main.log.noteToken(tok);
        
        if (curToken == null) {
        	curToken = tok;
        } else if (curToken != null && nextToken == null) {
        	nextToken = tok;
        } else {
        	curToken = nextToken;
        	nextToken = tok;
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

    // Source test utilities:

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

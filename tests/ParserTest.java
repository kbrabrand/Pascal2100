package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Scanner;

import org.junit.Test;

public class ParserTest {
    private static final String testFiles[] = {
        "10star",
        "mini",
        "gcd"
    };

    private void processFile(String name) {
        Runtime r = Runtime.getRuntime();

        try {
            Process p = r.exec("java -jar pascal2100.jar -testparser tests/fixtures/" + name + ".pas");
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTestFileContents(String name) throws Exception {
        @SuppressWarnings("resource")
        String content = new Scanner(new File("./tests/fixtures/" + name)).useDelimiter("\\Z").next();

        return content;
    }

    private void testProcessResult(String name) throws Exception {
        processFile(name);

        String actual = getTestFileContents(name + ".log");
        String expected = getTestFileContents(name + ".expected.parser");

        assertEquals(expected, actual);
        System.out.println("[Parser]  " + name + ": OK");
    }

    @Test
    public void evaluatesExpression() throws Exception {
        for (int i = 0; i < testFiles.length; i++) {
            testProcessResult(testFiles[i]);
        }
    }
}
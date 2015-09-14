package tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.junit.Test;

public class ScannerTest {
	private static final String testFiles[] = {
		"mini", 
		"gcd",
		"nested_comment",
		"endless_comment",
		"endless_string_literal",
		"illegal_character"
	};
	
	private String processFile(String name) {
		Runtime r = Runtime.getRuntime();
		String output = "";
		try {
			Process p = r.exec("java -jar pascal2100.jar -testscanner ./test_files/" + name + ".pas");
			p.waitFor();
			
			BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;

			while ((line = b.readLine()) != null) {
				System.out.println(line);
				
				output += line;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
	
	private String getTestFileContents(String name) throws Exception {
		@SuppressWarnings("resource")
		String content = new Scanner(new File("./test_files/" + name)).useDelimiter("\\Z").next();
		
		return content;
	}
	
	private void testProcessResult(String name) throws Exception {
		processFile(name);
		
		String actual = getTestFileContents(name + ".log");
		String expected = getTestFileContents(name + ".expected");
		
		assertEquals(expected, actual);
		System.out.println(name + ": OK");
	}
	
	@Test
    public void evaluatesExpression() throws Exception {
		for (int i = 0; i < testFiles.length; i++) {
			testProcessResult(testFiles[i]);
		}
    }
}
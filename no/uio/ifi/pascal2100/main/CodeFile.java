package no.uio.ifi.pascal2100.main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeFile {
    private String codeFileName;
    private PrintWriter code;
    private int numLabels = 0;

    CodeFile(String fName) {
        codeFileName = fName;

        try {
            code = new PrintWriter(fName);
        } catch (FileNotFoundException e) {
            Main.error("Cannot create code file " + fName + "!");
        }

        code.println("# Code file created by Pascal2100 compiler " +
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    void finish() {
        code.close();
    }

    public String identify() {
        return "Code file named " + codeFileName;
    }


    public String getLabel(String origName) {
        return origName + "_" + (++numLabels);
    }

    public String getLocalLabel() {
        return String.format(".L%04d", ++numLabels);
    }

    private void printLabel(String lab, boolean justALabel) {
        if (lab.length() > 6) {
            code.print(lab + ":");

            if (!justALabel) {
                code.print("\n        ");
            }
        } else if (lab.length() > 0) {
            code.printf("%-8s", lab+":");
        } else {
            code.print("        ");
        }
    }

    public void genDirective(String directive, String param) {
        code.printf("%-7s %-7s %-15s", " ", directive, param);
        code.println();
    }

    public void genInstr(String lab, String instr, String arg, String comment) {
        printLabel(lab, (instr+arg+comment).equals(""));
        code.printf("%-7s %-23s ", instr, arg);

        if (comment.length() > 0) {
            code.print("# " + comment);
        }

        code.println();
    }

    public void genInstr(String instr, String arg, String comment) {
        this.genInstr("", instr, arg, comment);
    }

    public void genInstr(String instr, String arg) {
        this.genInstr(instr, arg, "");
    }

    public void genString(String name, String s, String comment) {
        genDirective(".data", "");
        printLabel(name, false);
        code.printf(".asciz   \"");

        for (int i = 0;  i < s.length();  i++) {
            if (s.charAt(i) == '"') {
                code.print("\\");
            }

            code.print(s.charAt(i));
        }

        code.print("\"");

        if (comment.length() > 0) {
            code.print("# " + comment);
        }

        code.println();
        genDirective(".align", "2");
        genDirective(".text", "");
    }
}

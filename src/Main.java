import java.io.*;
import java.util.*;

import common.NiftyException;

import a.SourceFile;

import parse.SourceFileParser;
import util.FileUtils;

public class Main {
    private static final boolean
            printA = true,
            printB = true,
            printC = true,
            printD = true;

    public static void main(String[] args) throws IOException {
        // Parse source files.
        List<SourceFile> sourceFiles = new ArrayList<SourceFile>();
        for (String sourceFileName : args) {
            String source = FileUtils.readFileAsUTF8(new File(sourceFileName));
            try {
                SourceFile sf = SourceFileParser.singleton.parse(source, 0).value;
                sourceFiles.add(sf);
            } catch (RuntimeException e) {
                throw new NiftyException(e, "Parse error in file %s.", sourceFileName);
            }
        }

        // Create a project from the source files.
        a.Project aProj = new a.Project(sourceFiles.toArray(new SourceFile[sourceFiles.size()]));
        if (printA) {
            System.out.println("--- PROJECT A ---");
            System.out.println(aProj);
            System.out.println();
        }

        // Refine (a -> b).
        b.Project bProj = aProj.refine();
        if (printB) {
            System.out.println("--- PROJECT B ---");
            System.out.println(bProj);
            System.out.println();
        }

        // Refine (b -> c).
        c.Project cProj = bProj.refine();
        if (printC) {
            System.out.println("--- PROJECT C ---");
            System.out.print(cProj);
            System.out.println();
        }

        // Compile (c -> d)!
        d.TypeDef[] dProj = cProj.compile();
        if (printD) {
            System.out.println("--- PROJECT D ---");
            for (d.TypeDef typeDef : dProj) {
                System.out.println(typeDef);
            }
            System.out.println();
        }
    }
}

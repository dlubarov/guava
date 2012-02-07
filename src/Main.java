import java.io.*;
import java.util.*;

import common.NiftyException;

import a.SourceFile;

import parse.SourceFileParser;
import util.FileUtils;

public class Main {
    public static void main(String[] args) throws IOException {
        // Parse source files
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
        a.Project aProj = new a.Project(sourceFiles.toArray(new SourceFile[sourceFiles.size()]));
        System.out.println(aProj);

        // Refine
    }
}

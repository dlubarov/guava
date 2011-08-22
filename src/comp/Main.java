package comp;

import static util.StringUtils.implode;

import java.io.*;

import ast.SourceFile;
import ctx.GlobalContext;

public class Main {
    private static SourceFile parseFile(String fname) throws IOException {
        Reader r = new FileReader(fname);
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = r.read()) != -1)
            sb.append((char) c);
        Parser p = new Parser(sb);
        SourceFile src = p.parseSourceFile();
        src.fname = fname;
        return src;
    }

    public static void main(String[] args) throws IOException {
        SourceFile[] sources = new SourceFile[args.length];
        for (int i = 0; i < args.length; ++i) {
            try {
                sources[i] = parseFile(args[i]);
            } catch (RuntimeException e) {
                System.err.printf("Parse error in file %s:\n", args[i]);
                throw e;
            }
        }
        for (SourceFile src : sources) {
            //System.out.println(src);
        }
        rst.TypeDef[] types = GlobalContext.refine(sources);
        System.out.println(implode("\n\n", types));
    }
}

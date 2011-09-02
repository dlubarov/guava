package comp;

import static util.StringUtils.implode;

import java.io.*;

import rctx.GlobalRCtx;

import ast.SourceFile;
import ctx.GlobalContext;

public class Main {
    private static final boolean PRINT_AST = false, PRINT_RST = true;
    
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
        System.out.println("Parsing source files...");
        SourceFile[] sources = new SourceFile[args.length];
        for (int i = 0; i < args.length; ++i) {
            try {
                sources[i] = parseFile(args[i]);
            } catch (RuntimeException e) {
                System.err.printf("Parse error in file %s:\n", args[i]);
                throw e;
            }
        }
        if (PRINT_AST)
            System.out.println(implode("\n\n", sources));
        
        System.out.println("Refining...");
        rst.TypeDef[] allTypes = GlobalContext.refine(sources);
        if (PRINT_RST)
            System.out.println(implode("\n\n", allTypes));
        
        System.out.println("Compiling...");
        GlobalRCtx.compile(allTypes);
        
        System.out.println("All done.");
    }
}

package comp;

import static util.StringUtils.implode;

import java.io.*;

import common.RawMethodDesc;

import rctx.GlobalRCtx;
import vm.God;
import vm.ty.ConcreteType;

import ast.SourceFile;
import ctx.GlobalContext;

public class Main {
    private static final boolean PRINT_AST = false, PRINT_RST = false;

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
        rst.TypeDef[] allTypeDefs = GlobalContext.refine(sources);
        if (PRINT_RST)
            System.out.println(implode("\n\n", allTypeDefs));

        System.out.println("Compiling...");
        vm.Type[] allTypes = GlobalRCtx.compile(allTypeDefs);

        System.out.println("Linking...");
        God.linkAll(allTypes);

        for (vm.Type type : allTypes) {
            for (vm.Method meth : type.ownedMethods) {
                RawMethodDesc desc = meth.desc;
                if (desc.name.equals("main") && desc.isStatic && desc.numGenericParams == 0) {
                    System.out.printf("Running %s.main...\n", type.desc);
                    meth.invoke(ConcreteType.NONE);
                }
            }
        }

        System.out.println("All done.");
    }
}

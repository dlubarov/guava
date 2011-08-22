package ast;

import ctx.FileContext;
import ctx.TypeContext;

import static util.StringUtils.implode;

public class SourceFile {
    public final String module;
    public final Import[] imports;
    public final TypeDef[] types;
    public String fname;

    public SourceFile(String module, Import[] imports, TypeDef[] types) {
        this.module = module;
        this.imports = imports;
        this.types = types;
    }

    public rst.TypeDef[] refine(FileContext ctx) {
        rst.TypeDef[] result = new rst.TypeDef[types.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = types[i].refine(new TypeContext(ctx, types[i]));
        return result;
    }
    
    public String toString() {
        return String.format("module %s;\n\n%s%s%s",
                module,
                implode('\n', imports),
                imports.length * types.length == 0 ? "" : "\n\n",
                implode("\n\n", types));
    }
}
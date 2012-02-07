package a;

import common.NiftyException;

public class SourceFile {
    public final String module;
    public final Import[] imports;
    public final TypeDef[] typeDefs;

    public SourceFile(String module, Import[] imports, TypeDef[] typeDefs) {
        this.module = module;
        this.imports = imports;
        this.typeDefs = typeDefs;
    }

    public b.TypeDef[] refine() {
        b.TypeDef[] refinedTypeDefs = new b.TypeDef[typeDefs.length];
        for (int i = 0; i < refinedTypeDefs.length; ++i)
            try {
                refinedTypeDefs[i] = typeDefs[i].refine(this);
            } catch (RuntimeException e) {
                throw new NiftyException(e, "A -> B refinement error in type '%s'.", typeDefs[i].name);
            }
        return refinedTypeDefs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("module ").append(module).append(';');
        for (TypeDef typeDef : typeDefs)
            sb.append("\n\n").append(typeDef);
        return sb.toString();
    }
}

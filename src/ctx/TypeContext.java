package ctx;

import ast.TypeDef;
import common.FullTypeDesc;
import common.RawTypeDesc;
import common.TypeGenericFullTypeDesc;

public class TypeContext extends Resolver {
    private final FileContext fileCtx;
    private final TypeDef type;

    public TypeContext(FileContext fileCtx, TypeDef type) {
        this.fileCtx = fileCtx;
        this.type = type;
    }

    public RawTypeDesc desc() {
        return new RawTypeDesc(fileCtx.localModule, type.name);
    }

    public RawTypeDesc resolveRaw(String typeName) {
        return fileCtx.resolveRaw(typeName);
    }

    @Override
    public FullTypeDesc resolveFull(String typeName) {
        if (type.isGenericParam(typeName))
            return new TypeGenericFullTypeDesc(desc(), type.genericParamIdx(typeName));
        return super.resolveFull(typeName);
    }
}

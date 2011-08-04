package ctx;

import ast.MethodDef;
import ast.Type;
import common.*;

import java.util.Arrays;

public class MethodContext extends Resolver {
    private final TypeContext typeCtx;
    public final MethodDef method;
    private final RawMethodDesc desc;

    public MethodContext(TypeContext typeCtx, MethodDef method) {
        this.typeCtx = typeCtx;
        this.method = method;
        desc = new RawMethodDesc(typeCtx.desc(), method.self.name, null);
        fixDesc();
    }

    private void fixDesc() {
        Type[] paramTypes = new Type[method.params.length];
        for (int i = 0; i < paramTypes.length; ++i)
            paramTypes[i] = method.params[i].type;
        desc.paramTypes = resolveAllFull(paramTypes);
    }

    public RawTypeDesc resolveRaw(String typeName) {
        return typeCtx.resolveRaw(typeName);
    }

    @Override
    public FullTypeDesc resolveFull(String typeName) {
        if (method.isGenericParam(typeName))
            return new MethodGenericFullTypeDesc(desc, method.genericParamIdx(typeName));
        return typeCtx.resolveFull(typeName);
    }
}

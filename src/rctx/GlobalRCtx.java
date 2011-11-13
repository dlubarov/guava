package rctx;

import java.util.*;

import rst.TypeDef;
import vm.Type;

import common.RawTypeDesc;

public class GlobalRCtx {
    private final Map<RawTypeDesc, TypeDef> allTypesByDesc;

    public GlobalRCtx(TypeDef[] allTypes) {
        allTypesByDesc = new HashMap<RawTypeDesc, TypeDef>();
        for (TypeDef type : allTypes)
            allTypesByDesc.put(type.desc, type);
    }

    public TypeDef resolve(RawTypeDesc desc) {
        return allTypesByDesc.get(desc);
    }

    // TODO: is there a more natural place for the top-level compile method?
    public static Type[] compile(TypeDef[] allTypes) {
        GlobalRCtx ctx = new GlobalRCtx(allTypes);
        Type[] result = new Type[allTypes.length];
        for (int i = 0; i < result.length; ++i)
            try {
                result[i] = allTypes[i].compile(ctx);
            } catch (Throwable e) {
                throw new RuntimeException("Compilation error in type " + allTypes[i].desc, e);
            }
        return result;
    }
}

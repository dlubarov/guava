package rctx;

import java.util.*;

import common.FullTypeDesc;
import common.RawMethodDesc;
import common.RawTypeDesc;
import rst.TypeDef;

public class CodeRCtx {
    private final MethodRCtx methodCtx;
    private final Map<Integer, FullTypeDesc> localTypes;
    
    public CodeRCtx(MethodRCtx methodCtx) {
        this.methodCtx = methodCtx;
        localTypes = new HashMap<Integer, FullTypeDesc>();
    }
    
    public CodeRCtx(CodeRCtx source) {
        methodCtx = source.methodCtx;
        localTypes = new HashMap<Integer, FullTypeDesc>(source.localTypes);
    }
    
    public TypeDef resolve(RawTypeDesc desc) {
        return methodCtx.resolve(desc);
    }
    
    public FullTypeDesc getLocalType(int index) {
        assert localTypes.containsKey(index) : "local index is out of bounds";
        return localTypes.get(index);
    }
    
    // Get the index of some type in the current method's type table
    public int getTypeIndex(RawTypeDesc desc) {
        return methodCtx.getTypeIndex(desc);
    }
    
    // Get the index of some method in the current method's method table
    public int getMethodIndex(RawMethodDesc desc) {
        return methodCtx.getMethodIndex(desc);
    }
    
    public CodeRCtx addLocal(int index, FullTypeDesc type) {
        CodeRCtx copy = new CodeRCtx(this);
        if (copy.localTypes.put(index, type) != null)
            throw new RuntimeException(String.format("two types for local %d", index));
        methodCtx.localIsUsed(index);
        return copy;
    }
}

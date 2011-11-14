package rctx;

import java.util.*;

import common.*;
import rst.TypeDef;

public class CodeRCtx {
    public final MethodRCtx methodCtx;
    private final Map<Integer, FullTypeDesc> localTypes;

    public CodeRCtx(MethodRCtx methodCtx, FullTypeDesc[] initLocals) {
        this.methodCtx = methodCtx;
        localTypes = new HashMap<Integer, FullTypeDesc>();
        for (int i = 0; i < initLocals.length; ++i)
            addLocalMutating(i, initLocals[i]);
    }

    public CodeRCtx(CodeRCtx source) {
        methodCtx = source.methodCtx;
        localTypes = new HashMap<Integer, FullTypeDesc>(source.localTypes);
    }

    public TypeDef resolve(RawTypeDesc desc) {
        return methodCtx.resolve(desc);
    }

    public FullTypeDesc getLocalType(int index) {
        assert localTypes.containsKey(index) : "local index " + index + " is out of bounds";
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

    // Get the index of a complete (parameterized) type in the current method's table
    public int getFullTypeIndex(FullTypeDesc desc) {
        return methodCtx.getFullTypeIndex(desc);
    }

    private void addLocalMutating(int index, FullTypeDesc type) {
        if (localTypes.put(index, type) != null)
            throw new RuntimeException(String.format("two types for local %d", index));
        methodCtx.localIsUsed(index);
    }

    public CodeRCtx addLocal(int index, FullTypeDesc type) {
        CodeRCtx copy = new CodeRCtx(this);
        copy.addLocalMutating(index, type);
        return copy;
    }
}

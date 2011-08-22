package rctx;

import java.util.*;

import common.FullTypeDesc;
import common.RawMethodDesc;
import common.RawTypeDesc;
import rst.TypeDef;

public class CodeRCtx {
    private final Map<Integer, FullTypeDesc> localTypes;
    private final List<RawTypeDesc> typeTable;
    private final List<RawMethodDesc> methodTable;
    
    public CodeRCtx() {
        localTypes = new HashMap<Integer, FullTypeDesc>();
        typeTable = new ArrayList<RawTypeDesc>();
        methodTable = new ArrayList<RawMethodDesc>();
    }
    
    public CodeRCtx(CodeRCtx source) {
        localTypes = new HashMap<Integer, FullTypeDesc>(source.localTypes);
        typeTable = new ArrayList<RawTypeDesc>(source.typeTable);
        methodTable = new ArrayList<RawMethodDesc>(source.methodTable);
    }
    
    public TypeDef resolve(RawTypeDesc desc) {
        throw new UnsupportedOperationException("FIXME: impl");
    }
    
    public FullTypeDesc getLocalType(int index) {
        return localTypes.get(index);
    }
    
    // Get the index of some type in the current method's type table
    public int getTypeIndex(RawTypeDesc desc) {
        throw new RuntimeException("FIXME: needs to be bound to the whole method");
//        int idx = typeTable.indexOf(desc);
//        if (idx == -1) {
//            typeTable.add(desc);
//            return typeTable.size() - 1;
//        }
//        return idx;
    }

    // Get the index of some method in the current method's method table
    public int getMethodIndex(RawMethodDesc desc) {
        throw new RuntimeException("FIXME: needs to be bound to the whole method");
//        int idx = methodTable.indexOf(desc);
//        if (idx == -1) {
//            methodTable.add(desc);
//            return methodTable.size() - 1;
//        }
//        return idx;
    }
    
    public CodeRCtx addLocal(int index, FullTypeDesc type) {
        CodeRCtx copy = new CodeRCtx(this);
        if (copy.localTypes.put(index, type) != null)
            throw new RuntimeException(String.format("two types for local %d", index));
        return copy;
    }
}

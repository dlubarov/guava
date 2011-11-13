package rctx;

import java.util.*;

import rst.TypeDef;

import common.*;

public class MethodRCtx {
    private final GlobalRCtx globalCtx;
    public final NormalFullTypeDesc owner;
    private final List<RawTypeDesc> typeTable;
    private final List<RawMethodDesc> methodTable;
    private int highestLocal = -1;

    public MethodRCtx(GlobalRCtx globalCtx, NormalFullTypeDesc owner) {
        this.globalCtx = globalCtx;
        this.owner = owner;
        typeTable = new ArrayList<RawTypeDesc>();
        methodTable = new ArrayList<RawMethodDesc>();
    }

    public TypeDef resolve(RawTypeDesc desc) {
        return globalCtx.resolve(desc);
    }

    // Get the index of some type in the current method's type table
    public int getTypeIndex(RawTypeDesc desc) {
        int idx = typeTable.indexOf(desc);
        if (idx == -1) {
            typeTable.add(desc);
            return typeTable.size() - 1;
        }
        return idx;
    }

    // Get the index of some method in the current method's method table
    public int getMethodIndex(RawMethodDesc desc) {
        int idx = methodTable.indexOf(desc);
        if (idx == -1) {
            methodTable.add(desc);
            return methodTable.size() - 1;
        }
        return idx;
    }

    public RawTypeDesc[] getTypeTable() {
        return typeTable.toArray(new RawTypeDesc[typeTable.size()]);
    }

    public RawMethodDesc[] getMethodTable() {
        return methodTable.toArray(new RawMethodDesc[methodTable.size()]);
    }

    public void localIsUsed(int index) {
        highestLocal = Math.max(highestLocal, index);
    }

    public int numLocals() {
        return highestLocal + 1;
    }
}

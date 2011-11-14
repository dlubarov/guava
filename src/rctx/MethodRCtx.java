package rctx;

import java.util.*;

import rst.TypeDef;
import vm.ty.*;

import common.*;

public class MethodRCtx {
    public final GlobalRCtx globalCtx;
    public final NormalFullTypeDesc owner;
    private final List<RawTypeDesc> typeTable;
    private final List<RawMethodDesc> methodTable;
    private final List<FullType> fullTypeTable;
    private int highestLocal = -1;

    public MethodRCtx(GlobalRCtx globalCtx, NormalFullTypeDesc owner) {
        this.globalCtx = globalCtx;
        this.owner = owner;
        typeTable = new ArrayList<RawTypeDesc>();
        methodTable = new ArrayList<RawMethodDesc>();
        fullTypeTable = new ArrayList<FullType>();
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

    private FullType toVMType(FullTypeDesc desc) {
        if (desc instanceof NormalFullTypeDesc) {
            NormalFullTypeDesc ndesc = (NormalFullTypeDesc) desc;
            FullType[] genericArgs = new FullType[ndesc.genericArgs.length];
            for (int i = 0; i < genericArgs.length; ++i)
                genericArgs[i] = toVMType(ndesc.genericArgs[i]);
            return new ExternalType(getTypeIndex(ndesc.raw), genericArgs);
        } else if (desc instanceof TypeGenericFullTypeDesc) {
            return new TypeGenericType(((TypeGenericFullTypeDesc) desc).index);
        } else if (desc instanceof MethodGenericFullTypeDesc) {
            return new MethodGenericType(((MethodGenericFullTypeDesc) desc).index);
        } else
            throw new RuntimeException("unknown subclass");
    }

    // Get the index of a complete (parameterized) type in the current method's table
    public int getFullTypeIndex(FullTypeDesc desc) {
        FullType type = toVMType(desc);
        int idx = fullTypeTable.indexOf(type);
        if (idx == -1) {
            fullTypeTable.add(type);
            return fullTypeTable.size() - 1;
        }
        return idx;
    }

    public RawTypeDesc[] getTypeTable() {
        return typeTable.toArray(new RawTypeDesc[typeTable.size()]);
    }

    public RawMethodDesc[] getMethodTable() {
        return methodTable.toArray(new RawMethodDesc[methodTable.size()]);
    }

    public FullType[] getFullTypeTable() {
        return fullTypeTable.toArray(new FullType[fullTypeTable.size()]);
    }

    public void localIsUsed(int index) {
        highestLocal = Math.max(highestLocal, index);
    }

    public int numLocals() {
        return highestLocal + 1;
    }
}

package rctx;

import java.util.*;

import rst.*;
import vm.ty.*;

import common.*;

public class MethodRCtx {
    public final GlobalRCtx globalCtx;
    public final NormalFullTypeDesc owner;
    public final MethodDef meth;
    private final List<RawTypeDesc> typeTable;
    private final List<RawMethodDesc> methodTable;
    private final List<FullType> fullTypeTable;
    private final List<String> stringTable;
    private int highestLocal = -1;

    public MethodRCtx(GlobalRCtx globalCtx, NormalFullTypeDesc owner, MethodDef meth) {
        this.globalCtx = globalCtx;
        this.owner = owner;
        this.meth = meth;

        typeTable = new ArrayList<RawTypeDesc>();
        methodTable = new ArrayList<RawMethodDesc>();
        fullTypeTable = new ArrayList<FullType>();
        stringTable = new ArrayList<String>();
    }

    public TypeDef resolve(RawTypeDesc desc) {
        return globalCtx.resolve(desc);
    }

    // Get the index of some type in the current method's type table
    public int getTypeIndex(RawTypeDesc desc) {
        int idx = typeTable.indexOf(desc);
        if (idx == -1) {
            idx = typeTable.size();
            typeTable.add(desc);
        }
        return idx;
    }

    // Get the index of some method in the current method's method table
    public int getMethodIndex(RawMethodDesc desc) {
        int idx = methodTable.indexOf(desc);
        if (idx == -1) {
            idx = methodTable.size();
            methodTable.add(desc);
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
            TypeGenericFullTypeDesc tgDesc = (TypeGenericFullTypeDesc) desc;
            return new TypeGenericType(getTypeIndex(tgDesc.owner), tgDesc.index);
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
            idx = fullTypeTable.size();
            fullTypeTable.add(type);
        }
        return idx;
    }

    public int getStringIndex(String s) {
        int idx = stringTable.indexOf(s);
        if (idx == -1) {
            idx = stringTable.size();
            stringTable.add(s);
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

    public String[] getStringTabale() {
        return stringTable.toArray(new String[stringTable.size()]);
    }

    public void localIsUsed(int index) {
        highestLocal = Math.max(highestLocal, index);
    }

    public int numLocals() {
        return highestLocal + 1;
    }
}

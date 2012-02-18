package c;

import java.util.*;

import common.*;

import c.ty.Type;
import c.gen.MethodGenericInfo;
import c.stm.*;

public class MethodDef {
    public final RawType owner;

    public final MethodVisibility visibility;
    public final boolean isStatic, isSealed;
    public final Type returnType;
    public final String name;
    public final MethodGenericInfo[] genericInfos;
    public final Type[] paramTypes;
    public final String[] paramNames;
    public final Block body;

    private final List<RawType> rawTypeTable;
    private final List<Type> fullTypeTable;
    private final List<MethodDef> methodTable;
    private final List<String> stringTable;

    public MethodDef(RawType owner,
            MethodVisibility visibility, boolean isStatic, boolean isSealed,
            Type returnType, String name, MethodGenericInfo[] genericInfos,
            Type[] paramTypes, String[] paramNames, Block body) {
        this.owner = owner;
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isSealed = isSealed;
        this.returnType = returnType;
        this.name = name;
        this.genericInfos = genericInfos;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
        this.body = body;

        rawTypeTable = new ArrayList<RawType>();
        fullTypeTable = new ArrayList<Type>();
        methodTable = new ArrayList<MethodDef>();
        stringTable = new ArrayList<String>();
    }

    public int getRawTypeTableIndex(RawType t) {
        for (int i = 0; i < rawTypeTable.size(); ++i)
            if (rawTypeTable.get(i).equals(t))
                return i;
        rawTypeTable.add(t);
        return rawTypeTable.size() - 1;
    }

    public int getFullTypeTableIndex(Type t) {
        for (int i = 0; i < fullTypeTable.size(); ++i)
            if (fullTypeTable.get(i).equals(t))
                return i;
        fullTypeTable.add(t);
        return fullTypeTable.size() - 1;
    }

    public int[] getFullTypeTableIndices(Type[] types) {
        int[] result = new int[types.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = getFullTypeTableIndex(types[i]);
        return result;
    }

    public int getMethodTableIndex(MethodDef m) {
        for (int i = 0; i < methodTable.size(); ++i)
            if (methodTable.get(i).equals(m))
                return i;
        methodTable.add(m);
        return methodTable.size() - 1;
    }

    public int getStringTableIndex(String s) {
        for (int i = 0; i < stringTable.size(); ++i)
            if (stringTable.get(i).equals(s))
                return i;
        stringTable.add(s);
        return stringTable.size() - 1;
    }

    private d.ty.desc.TypeDesc[] refineParamTypes() {
        d.ty.desc.TypeDesc[] result = new d.ty.desc.TypeDesc[paramTypes.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = paramTypes[i].refine();
        return result;
    }

    private d.RawMethod refineDesc() {
        return new d.RawMethod(isStatic, owner, name, genericInfos.length, refineParamTypes());
    }

    private d.ty.desc.TypeDesc[] refineFullTypeTable() {
        d.ty.desc.TypeDesc[] result = new d.ty.desc.TypeDesc[fullTypeTable.size()];
        for (int i = 0; i < result.length; ++i)
            result[i] = fullTypeTable.get(i).refine();
        return result;
    }

    private d.RawMethod[] refineMethodTable() {
        d.RawMethod[] result = new d.RawMethod[methodTable.size()];
        for (int i = 0; i < result.length; ++i)
            result[i] = methodTable.get(i).refineDesc();
        return result;
    }

    public d.MethodDef compile(TypeDef owner) {
        if (body == null)
            return new d.AbstractMethodDef(refineDesc());

        CodeContext ctx = new CodeContext(owner, this);
        CompilationResult result = body.compile(ctx);
        int numLocals = result.newCtx.getNumLocals();
        int[] bytecode = result.code.getCode();

        return new d.BytecodeMethodDef(
                refineDesc(),
                rawTypeTable.toArray(new RawType[rawTypeTable.size()]),
                refineFullTypeTable(),
                refineMethodTable(),
                stringTable.toArray(new String[stringTable.size()]),
                numLocals,
                bytecode);
    }

    private String qualsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(visibility);
        if (isStatic)
            sb.append("static ");
        if (isSealed)
            sb.append("sealed ");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(qualsString());
        sb.append(returnType).append(' ').append(name);

        // Append generic infos.
        if (genericInfos.length > 0)
            sb.append(Arrays.toString(genericInfos));

        // Append parameter list.
        sb.append('(');
        for (int i = 0; i < paramNames.length; ++i) {
            if (i > 0)
                sb.append(", ");
            sb.append(paramTypes[i]).append(' ').append(paramNames[i]);
        }
        sb.append(')');

        // Append the method body.
        if (body == null)
            sb.append(';');
        else
            sb.append(' ').append(body);

        return sb.toString();
    }
}

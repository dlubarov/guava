package common;

import java.util.Arrays;

import rctx.GlobalRCtx;
import static util.StringUtils.implode;

public class RawMethodDesc {
    public final RawTypeDesc owner;
    public final String name;
    public final int numGenericParams;
    public FullTypeDesc[] paramTypes;
    public final boolean isStatic;

    public RawMethodDesc(RawTypeDesc owner, String name,
                         int numGenericParams, FullTypeDesc[] paramTypes,
                         boolean isStatic) {
        this.owner = owner;
        this.name = name;
        this.numGenericParams = numGenericParams;
        this.paramTypes = paramTypes;
        this.isStatic = isStatic;
    }

    public RawMethodDesc(RawTypeDesc owner, String name,
                         int numGenericParams, FullTypeDesc[] paramTypes) {
        this(owner, name, numGenericParams, paramTypes, false);
    }

    public RawMethodDesc(String module, String owner, String name, int numGenericParams, FullTypeDesc[] paramTypes) {
        this(new RawTypeDesc(module, owner), name, numGenericParams, paramTypes, false);
    }

    public RawMethodDesc withTypeGenerics(FullTypeDesc[] typeGenerics) {
        FullTypeDesc[] newParamTypes = new FullTypeDesc[paramTypes.length];
        for (int i = 0; i < newParamTypes.length; ++i)
            newParamTypes[i] = paramTypes[i].withTypeGenerics(typeGenerics);
        return new RawMethodDesc(owner, name, numGenericParams, newParamTypes, isStatic);
    }

    public boolean canOverride(GlobalRCtx ctx, RawMethodDesc m) {
        if (isStatic || m.isStatic)
            return false;
        if (!owner.equals(m.owner) || !name.equals(m.name))
            return false;
        if (numGenericParams != m.numGenericParams)
            return false;
        for (int i = 0; i < paramTypes.length; ++i)
            if (!m.paramTypes[i].isSubtype(paramTypes[i], ctx))
                return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        try {
            RawMethodDesc that = (RawMethodDesc) o;
            return owner.equals(that.owner) && name.equals(that.name)
                    && numGenericParams == that.numGenericParams
                    && Arrays.equals(paramTypes, that.paramTypes)
                    && isStatic == that.isStatic;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {owner, name, numGenericParams, paramTypes.length});
    }

    private String genericParamString() {
        if (numGenericParams == 0)
            return "";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < numGenericParams; ++i) {
            if (i > 0)
                sb.append(", ");
            sb.append('T').append(i);
        }
        return sb.append(']').toString();
    }

    public String toString() {
        return String.format("%s%s.%s%s(%s)",
                isStatic? "static " : "",
                owner, name,
                genericParamString(),
                implode(", ", paramTypes));
    }
}

package common;

import java.util.Arrays;

public class RawMethodDesc {
    private final RawTypeDesc owner;
    private final String name;
    private final int numGenericParams;
    public FullTypeDesc[] paramTypes;

    public RawMethodDesc(RawTypeDesc owner, String name, int numGenericParams, FullTypeDesc[] paramTypes) {
        this.owner = owner;
        this.name = name;
        this.numGenericParams = numGenericParams;
        this.paramTypes = paramTypes;
    }

    public RawMethodDesc(String module, String owner, String name, int numGenericParams, FullTypeDesc[] paramTypes) {
        this(new RawTypeDesc(module, owner), name, numGenericParams, paramTypes);
    }

    @Override
    public boolean equals(Object o) {
        try {
            RawMethodDesc that = (RawMethodDesc) o;
            return owner.equals(that.owner) && name.equals(that.name)
                    && numGenericParams == that.numGenericParams
                    && Arrays.equals(paramTypes, that.paramTypes);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {owner, name, numGenericParams, paramTypes});
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
        return String.format("%s.%s%s%s",
                owner, name,
                genericParamString(),
                Arrays.toString(paramTypes));
    }
}

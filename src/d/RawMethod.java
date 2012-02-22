package d;

import java.util.Arrays;

import util.StringUtils;

import common.RawType;
import d.ty.desc.TypeDesc;

public class RawMethod {
    public static final RawMethod[] NONE = new RawMethod[0];

    public final boolean isStatic;
    public final RawType owner;
    public final String name;
    public final int numGenericParams;
    public final TypeDesc[] paramTypes;

    public RawMethod(boolean isStatic, RawType owner, String name,
            int numGenericParams, TypeDesc[] paramTypes) {
        this.isStatic = isStatic;
        this.owner = owner;
        this.name = name;
        this.numGenericParams = numGenericParams;
        this.paramTypes = paramTypes;
    }

    @Override
    public boolean equals(Object o) {
        try {
            RawMethod that = (RawMethod) o;
            return isStatic == that.isStatic
                    && owner.equals(that.owner)
                    && name.equals(that.name)
                    && numGenericParams == that.numGenericParams
                    && Arrays.equals(paramTypes, that.paramTypes);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return owner.hashCode() ^ name.hashCode();
    }

    @Override
    public String toString() {
        // TODO: more descriptive toString
        StringBuilder sb = new StringBuilder();
        if (isStatic)
            sb.append("static ");
        sb.append(owner).append('.').append(name);
        sb.append('(').append(StringUtils.implode(", ", paramTypes)).append(')');
        return sb.toString();
    }
}

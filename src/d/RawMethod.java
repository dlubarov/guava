package d;

import java.util.Arrays;

import util.StringUtils;

import common.RawType;
import d.ty.desc.TypeDesc;

public class RawMethod {
    public static final RawMethod[] NONE = new RawMethod[0];

    public static final RawMethod
            coreEnumerable_enumerator = new RawMethod(false, RawType.coreEnumerable, "enumerator", 0, TypeDesc.NONE),
            coreSource_tryTake = new RawMethod(false, RawType.coreSource, "tryTake", 0, TypeDesc.NONE),
            coreCollection_isEmpty = new RawMethod(false, RawType.coreCollection, "isEmpty", 0, TypeDesc.NONE),
            coreMaybe_get = new RawMethod(false, RawType.coreMaybe, "get", 0, TypeDesc.NONE);

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
        StringBuilder sb = new StringBuilder();
        if (isStatic)
            sb.append("static ");
        sb.append(owner).append('.').append(name);
        if (numGenericParams > 0) {
            sb.append('[');
            for (int i = 0; i < numGenericParams; ++i) {
                if (i > 0)
                    sb.append(", ");
                sb.append('M').append(i);
            }
            sb.append(']');
        }
        sb.append('(').append(StringUtils.implode(", ", paramTypes)).append(')');
        return sb.toString();
    }
}

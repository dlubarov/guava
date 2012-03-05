package d.ty;

import java.util.Arrays;

import common.RawType;

import d.*;
import d.ty.sup.SuperType;

public class ConcreteType {
    public static final ConcreteType[] NONE = new ConcreteType[0];

    public final TypeDef rawType;
    public final ConcreteType[] genericArgs;

    public ConcreteType(TypeDef rawType, ConcreteType[] genericArgs) {
        assert rawType != null;
        this.rawType = rawType;
        this.genericArgs = genericArgs;
    }

    // Construct a concrete type with no generic arguments.
    public ConcreteType(TypeDef rawType) {
        this(rawType, NONE);
        assert rawType.genericVariances == null || rawType.genericVariances.length == 0 :
            rawType.desc + " requires generic arguments.";
    }

    public boolean isSubtype(ConcreteType that) {
        if (rawType.desc.equals(RawType.coreBottom))
            return true;

        SuperType[] myGenericArgsForThat = rawType.superGenerics.get(that.rawType);
        if (myGenericArgsForThat == null)
            return false; // the raw types don't even fit

        // Check each generic argument.
        for (int i = 0; i < myGenericArgsForThat.length; ++i) {
            ConcreteType myGenericArg = myGenericArgsForThat[i].toConcrete(genericArgs);
            ConcreteType requiredGenericArg = that.genericArgs[i];
            switch (that.rawType.genericVariances[i]) {
                case COVARIANT:
                    if (!myGenericArg.isSubtype(requiredGenericArg))
                        return false;
                    break;
                case NONVARIANT:
                    if (!myGenericArg.equals(requiredGenericArg))
                        return false;
                    break;
                case CONTRAVARIANT:
                    if (!requiredGenericArg.isSubtype(myGenericArg))
                        return false;
                    break;
            }
            // TODO: check generic bounds here.
        }

        // If we got here, the generic arguments all fit.
        return true;
    }

    public BaseObject rawInstance() {
        return rawType.rawInstance(this);
    }

    @Override
    public boolean equals(Object o) {
        try {
            ConcreteType that = (ConcreteType) o;
            return rawType == that.rawType && Arrays.equals(genericArgs, that.genericArgs);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return rawType.hashCode() ^ Arrays.hashCode(genericArgs);
    }

    @Override
    public String toString() {
        if (genericArgs.length == 0)
            return rawType.desc.toString();
        return rawType.desc + Arrays.toString(genericArgs);
    }
}

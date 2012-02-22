package d.ty.desc;

import java.util.Arrays;

import common.RawType;
import d.*;
import d.ty.nf.*;
import d.ty.sup.*;

public class ParameterizedTypeDesc extends TypeDesc {
    public final RawType rawType;
    public final TypeDesc[] genericArgs;

    public ParameterizedTypeDesc(RawType rawType, TypeDesc[] genericArgs) {
        this.rawType = rawType;
        this.genericArgs = genericArgs;
        assert genericArgs != null;
    }

    public ParameterizedTypeDesc(RawType rawType) {
        this(rawType, TypeDesc.NONE);
    }

    @Override
    public NonFinalType toNonFinal(TypeDef owner) {
        NonFinalType[] nonFinalGenericArgs = new NonFinalType[genericArgs.length];
        for (int i = 0; i < nonFinalGenericArgs.length; ++i)
            nonFinalGenericArgs[i] = genericArgs[i].toNonFinal(owner);
        return new ParameterizedNonFinalType(God.resolveType(rawType), nonFinalGenericArgs);
    }

    @Override
    public SuperType toSuper() {
        SuperType[] superGenericArgs = new SuperType[genericArgs.length];
        for (int i = 0; i < superGenericArgs.length; ++i)
            superGenericArgs[i] = genericArgs[i].toSuper();
        return new ParameterizedSuperType(God.resolveType(rawType), superGenericArgs);
    }

    @Override
    public boolean equals(Object o) {
        try {
            ParameterizedTypeDesc that = (ParameterizedTypeDesc) o;
            return rawType.equals(that.rawType) && Arrays.equals(genericArgs, that.genericArgs);
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
            return rawType.toString();
        return rawType + Arrays.toString(genericArgs);
    }
}

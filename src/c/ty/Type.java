package c.ty;

import java.util.*;

import c.*;

public abstract class Type {
    public static final Type[] NONE = new Type[0];

    public abstract Type withGenericArgs(Type[] typeGenericArgs, Type[] methodGenericArgs);

    public abstract Type[] getSupertypes(TypeDef typeCtx, MethodDef methodCtx);

    public abstract Type[] getSubtypes(TypeDef typeCtx, MethodDef methodCtx);

    public final ParameterizedType[] getConcreteSupertypes(TypeDef typeCtx, MethodDef methodCtx) {
        Set<ParameterizedType> result = new HashSet<ParameterizedType>();
        for (Type supertype : getSupertypes(typeCtx, methodCtx))
            if (supertype instanceof ParameterizedType)
                result.add((ParameterizedType) supertype);
            else for (Type grandfatherType : supertype.getSupertypes(typeCtx, methodCtx))
                result.addAll(Arrays.asList(grandfatherType.getConcreteSupertypes(typeCtx, methodCtx)));
        return result.toArray(new ParameterizedType[result.size()]);
    }

    public boolean isSubtype(Type that, TypeDef typeCtx, MethodDef methodCtx) {
        if (equals(that))
            return true;
        for (Type supertype : getSupertypes(typeCtx, methodCtx))
            if (supertype.isSubtype(that, typeCtx, methodCtx))
                return true;
        for (Type thatSubtype : that.getSubtypes(typeCtx, methodCtx))
            if (isSubtype(thatSubtype, typeCtx, methodCtx))
                return true;
        return false;
    }

    public final boolean isSupertype(Type that, TypeDef typeCtx, MethodDef methodCtx) {
        return that.isSubtype(this, typeCtx, methodCtx);
    }

    public final boolean isSubtype(Type that, CodeContext ctx) {
        return isSubtype(that, ctx.type, ctx.method);
    }

    public final boolean isSupertype(Type that, CodeContext ctx) {
        return isSupertype(that, ctx.type, ctx.method);
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}

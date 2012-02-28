package c.ty;

import java.util.*;

import common.RawType;
import c.*;

public abstract class Type {
    public static final Type[] NONE = new Type[0];

    public static final Type
            coreTop = new ParameterizedType(RawType.coreTop, NONE),
            coreUnit = new ParameterizedType(RawType.coreUnit, NONE),
            coreInt = new ParameterizedType(RawType.coreInt, NONE),
            coreBool = new ParameterizedType(RawType.coreBool, NONE);

    public abstract Type withGenericArgs(Type[] typeGenericArgs, Type[] methodGenericArgs);

    protected abstract Type[] getSupertypes(TypeDef typeCtx, MethodDef methodCtx);

    protected abstract Type[] getSubtypes(TypeDef typeCtx, MethodDef methodCtx);

    public ParameterizedType[] getConcreteSupertypes(TypeDef typeCtx, MethodDef methodCtx) {
        Set<ParameterizedType> result = new HashSet<ParameterizedType>();
        for (Type supertype : getSupertypes(typeCtx, methodCtx))
            if (supertype instanceof ParameterizedType)
                result.add((ParameterizedType) supertype);
            else for (Type grandfatherType : supertype.getSupertypes(typeCtx, methodCtx))
                result.addAll(Arrays.asList(grandfatherType.getConcreteSupertypes(typeCtx, methodCtx)));
        assert !result.isEmpty() : "list of concrete supertypes should at least contain core.Top";
        return result.toArray(new ParameterizedType[result.size()]);
    }

    public boolean isSubtype(Type that, TypeDef typeCtx, MethodDef methodCtx) {
        // TODO: inefficient search
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

    // Convert this to some supertype. Useful for getting the generic arguments associated with
    // some supertype. If we call String.asSupertype(Sequence), we should get Sequence[Char].
    public ParameterizedType asSupertype(RawType supertype, CodeContext ctx) {
        // Different supertypes may have different parameterizations.
        Set<ParameterizedType> parameterizations = new HashSet<ParameterizedType>();
        for (Type sup : getSupertypes(ctx.type, ctx.method))
            try {
                parameterizations.add(sup.asSupertype(supertype, ctx));
            } catch (IllegalArgumentException e) {}
        if (parameterizations.isEmpty())
            throw new IllegalArgumentException(String.format(
                    "supertype %s not found for %s", supertype, this));
        return (ParameterizedType) TypeUtils.intersectionNoBottom(
                parameterizations.toArray(new ParameterizedType[parameterizations.size()]),
                ctx.type, ctx.method);
    }

    public abstract d.ty.desc.TypeDesc refine();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}

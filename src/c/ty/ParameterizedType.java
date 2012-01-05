package c.ty;

import java.util.*;

import c.*;

import common.*;

public class ParameterizedType extends Type {
    public final RawType rawType;
    public final Type[] genericArgs;

    public ParameterizedType(RawType rawType, Type[] genericArgs) {
        this.rawType = rawType;
        this.genericArgs = genericArgs;
    }

    public ParameterizedType(RawType rawType) {
        this(rawType, Type.NONE);
    }

    public ParameterizedType(String module, String typeName) {
        this(new RawType(module, typeName));
    }

    @Override
    public ParameterizedType withGenericArgs(Type[] typeGenericArgs, Type[] methodGenericArgs) {
        Type[] newGenericArgs = new Type[genericArgs.length];
        for (int i = 0; i < newGenericArgs.length; ++i)
            newGenericArgs[i] = genericArgs[i].withGenericArgs(typeGenericArgs, methodGenericArgs);
        return new ParameterizedType(rawType, newGenericArgs);
    }

    @Override
    public Type[] getSupertypes(TypeDef typeCtx, MethodDef methodCtx) {
        assert rawType.equals(typeCtx.desc);
        Type[] result = new Type[typeCtx.parents.length];
        for (int i = 0; i < typeCtx.parents.length; ++i)
            result[i] = typeCtx.parents[i].withGenericArgs(genericArgs, null);
        return result;
    }

    @Override
    public Type[] getSubtypes(TypeDef typeCtx, MethodDef methodCtx) {
        // We don't actually need to find our child types, because they will find us
        // when their getSupertypes is called. So we just return Bottom.
        if (rawType.equals(RawType.coreBottom))
            return Type.NONE;
        return new Type[] {new ParameterizedType("core", "Bottom")};
    }

    @Override
    public boolean isSubtype(Type that, TypeDef typeCtx, MethodDef methodCtx) {
        assert rawType.equals(typeCtx.desc);
        if (that instanceof ParameterizedType) {
            ParameterizedType thatParamType = (ParameterizedType) that;
            if (rawType.equals(thatParamType.rawType)) {
                if (genericArgs.length != thatParamType.genericArgs.length)
                    throw new NiftyException("%s has wrong number of generic arguments", rawType);
                for (int i = 0; i < genericArgs.length; ++i) {
                    Variance var = typeCtx.genericInfos[i].var;
                    Type thisGenArg = genericArgs[i];
                    Type thatGenArg = thatParamType.genericArgs[i];
                    switch (var) {
                        case COVARIANT:
                            if (!thisGenArg.isSubtype(thatGenArg, typeCtx, methodCtx))
                                return false;
                            break;
                        case NONVARIANT:
                            if (!thisGenArg.equals(thatGenArg))
                                return false;
                            break;
                        case CONTRAVARIANT:
                            if (!thisGenArg.isSupertype(thatGenArg, typeCtx, methodCtx))
                                return false;
                            break;
                    }
                }
                return true;
            }
        }
        return super.isSubtype(that, typeCtx, methodCtx);
    }

    public ParameterizedType asSupertype(RawType supertype, CodeContext ctx) {
        if (rawType.equals(supertype))
            return this;
        TypeDef rawTypeDef = ctx.project.resolve(rawType);
        Set<ParameterizedType> supertypes = new HashSet<ParameterizedType>();
        for (ParameterizedType parent : rawTypeDef.parentsWithGenerics(genericArgs))
            try {
                supertypes.add(parent.asSupertype(supertype, ctx));
            } catch (IllegalArgumentException e) {}
        if (supertypes.isEmpty())
            throw new IllegalArgumentException(String.format(
                    "supertype %s not found for %s", supertype, this));
        return (ParameterizedType) TypeUtils.intersectionNoBottom(
                supertypes.toArray(new ParameterizedType[supertypes.size()]),
                ctx.type, ctx.method);
    }

    @Override
    public boolean equals(Object o) {
        try {
            ParameterizedType that = (ParameterizedType) o;
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
        return rawType + Arrays.toString(genericArgs);
    }
}

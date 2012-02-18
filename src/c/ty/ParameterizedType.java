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
        TypeDef myTypeDef = Project.singleton.resolve(rawType);
        return myTypeDef.parentsWithGenerics(genericArgs);
    }

    @Override
    public Type[] getSubtypes(TypeDef typeCtx, MethodDef methodCtx) {
        // We don't actually need to find our child types, because they will find us
        // when their getSupertypes is called. So we just return Bottom.
        if (rawType.equals(RawType.coreBottom))
            return Type.NONE;
        return new Type[] {new ParameterizedType(RawType.coreBottom)};
    }

    @Override
    public final ParameterizedType[] getConcreteSupertypes(TypeDef typeCtx, MethodDef methodCtx) {
        // I'm already a concrete type.
        return new ParameterizedType[] {this};
    }

    @Override
    public boolean isSubtype(Type that, TypeDef typeCtx, MethodDef methodCtx) {
        TypeDef myTypeDef = Project.singleton.resolve(rawType);
        if (that instanceof ParameterizedType) {
            ParameterizedType thatParamType = (ParameterizedType) that;
            if (rawType.equals(thatParamType.rawType)) {
                if (genericArgs.length != thatParamType.genericArgs.length)
                    throw new NiftyException("%s has wrong number of generic arguments", rawType);
                for (int i = 0; i < genericArgs.length; ++i) {
                    Variance var = myTypeDef.genericInfos[i].var;
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

    @Override
    public ParameterizedType asSupertype(RawType supertype, CodeContext ctx) {
        // If we're tying to convert myself to myself, well, return myself.
        if (rawType.equals(supertype))
            return this;
        // Otherwise, search my parent types as usual.
        return super.asSupertype(supertype, ctx);
    }

    @Override
    public d.ty.desc.ParameterizedTypeDesc refine() {
        d.ty.desc.TypeDesc[] refinedArgs = new d.ty.desc.TypeDesc[genericArgs.length];
        for (int i = 0; i < refinedArgs.length; ++i)
            refinedArgs[i] = genericArgs[i].refine();
        return new d.ty.desc.ParameterizedTypeDesc(rawType, refinedArgs);
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
        if (genericArgs.length == 0)
            return rawType.toString();
        return rawType + Arrays.toString(genericArgs);
    }
}

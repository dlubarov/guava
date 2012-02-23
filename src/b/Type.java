package b;

import java.util.Arrays;

import common.*;

public class Type {
    public static final Type[] NONE = new Type[0];

    public final String rawType;
    public final Type[] genericArgs;

    public Type(String rawType, Type[] genericArgs) {
        this.rawType = rawType;
        this.genericArgs = genericArgs;
    }

    public Type(String rawType) {
        this(rawType, NONE);
    }

    public c.ty.Type refine(TypeDef typeCtx, MethodDef methodCtx) {
        if (genericArgs.length == 0) {
            if (methodCtx != null) {
                int index = methodCtx.genericParamIndex(rawType);
                if (index != -1)
                    return new c.ty.MethodGenericType(index);
            }
            if (typeCtx != null) {
                int index = typeCtx.genericParameterIndex(rawType);
                if (index != -1)
                    return new c.ty.TypeGenericType(index);
            }
        }

        RawType refinedRawType = typeCtx.qualifyType(rawType);
        c.ty.Type[] refinedGenericArgs = new c.ty.Type[genericArgs.length];
        for (int i = 0; i < refinedGenericArgs.length; ++i)
            refinedGenericArgs[i] = genericArgs[i].refine(typeCtx, methodCtx);

        TypeDef rawTypeDef = Project.singleton.typeDefs.get(refinedRawType);
        if (genericArgs.length != rawTypeDef.genericParams.length)
            throw new NiftyException(
                    "%s was given %d generic arguments; expecting %d.",
                    refinedRawType, genericArgs.length, rawTypeDef.genericParams.length);

        return new c.ty.ParameterizedType(refinedRawType, refinedGenericArgs);
    }

    public static c.ty.Type[] refineAll(Type[] types, TypeDef typeCtx, MethodDef methodCtx) {
        c.ty.Type[] result = new c.ty.Type[types.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = types[i].refine(typeCtx, methodCtx);
        return result;
    }

    @Override
    public String toString() {
        if (genericArgs.length == 0)
            return rawType;
        return rawType + Arrays.toString(genericArgs);
    }
}

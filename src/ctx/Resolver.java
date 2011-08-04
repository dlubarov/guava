package ctx;

import ast.Type;
import common.*;

import java.util.NoSuchElementException;

public abstract class Resolver {
    public abstract RawTypeDesc resolveRaw(String typeName);

    public FullTypeDesc resolveFull(String typeName) {
        return new NormalFullTypeDesc(resolveRaw(typeName));
    }

    public final FullTypeDesc resolveFull(Type type) {
        if (type.genericArgs.length == 0)
            return resolveFull(type.name);
        FullTypeDesc[] genericArgs = new FullTypeDesc[type.genericArgs.length];
        for (int i = 0; i < genericArgs.length; ++i)
            genericArgs[i] = resolveFull(type.genericArgs[i]);
        return new NormalFullTypeDesc(resolveRaw(type.name), genericArgs);
    }

    public final FullTypeDesc[] resolveAllFull(Type[] types) {
        FullTypeDesc[] result = new FullTypeDesc[types.length];
        for (int i = 0; i < types.length; ++i)
            result[i] = resolveFull(types[i]);
        return result;
    }

    public final boolean hasRawType(String typeName) {
        try {
            resolveRaw(typeName);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public final boolean hasFullType(Type type) {
        try {
            resolveFull(type);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}

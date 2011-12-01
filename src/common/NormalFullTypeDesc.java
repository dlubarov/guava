package common;

import rctx.*;
import rst.TypeDef;

import java.util.Arrays;

public class NormalFullTypeDesc extends FullTypeDesc {
    public final RawTypeDesc raw;
    public final FullTypeDesc[] genericArgs;

    public NormalFullTypeDesc(RawTypeDesc raw, FullTypeDesc[] genericArgs) {
        this.raw = raw;
        this.genericArgs = genericArgs;
    }

    public NormalFullTypeDesc(RawTypeDesc raw) {
        this(raw, new FullTypeDesc[0]);
    }

    // Find this type's generic args for some (non-strict) supertype
    public FullTypeDesc[] genericsFor(RawTypeDesc superTypeDesc, GlobalRCtx ctx) {
        TypeDef myType = ctx.resolve(raw);
        TypeDef superType = ctx.resolve(superTypeDesc);
        int n = superType.genericInfos.length;
        FullTypeDesc[] result = new FullTypeDesc[n];
        for (int i = 0; i < n; ++i)
            result[i] = myType.inMyGenerics(new TypeGenericFullTypeDesc(superTypeDesc, i), genericArgs, ctx);
        return result;
    }

    public NormalFullTypeDesc withTypeGenerics(FullTypeDesc[] typeGenerics) {
        FullTypeDesc[] newGenericArgs = new FullTypeDesc[genericArgs.length];
        for (int i = 0; i < newGenericArgs.length; ++i)
            newGenericArgs[i] = genericArgs[i].withTypeGenerics(typeGenerics);
        return new NormalFullTypeDesc(raw, newGenericArgs);
    }

    public NormalFullTypeDesc withMethodGenerics(FullTypeDesc[] methodGenerics) {
        FullTypeDesc[] newGenericArgs = new FullTypeDesc[genericArgs.length];
        for (int i = 0; i < newGenericArgs.length; ++i)
            newGenericArgs[i] = genericArgs[i].withMethodGenerics(methodGenerics);
        return new NormalFullTypeDesc(raw, newGenericArgs);
    }

    public NormalFullTypeDesc withGenerics(FullTypeDesc[] typeGenerics, FullTypeDesc[] methodGenerics) {
        FullTypeDesc[] newGenericArgs = new FullTypeDesc[genericArgs.length];
        for (int i = 0; i < newGenericArgs.length; ++i)
            newGenericArgs[i] = genericArgs[i].withGenerics(typeGenerics, methodGenerics);
        return new NormalFullTypeDesc(raw, newGenericArgs);
    }

    public boolean isSubtype(FullTypeDesc td, GlobalRCtx ctx) {
        if (td instanceof TypeGenericFullTypeDesc || td instanceof MethodGenericFullTypeDesc)
            return td.isSupertype(this, ctx);

        NormalFullTypeDesc that = (NormalFullTypeDesc) td;
        TypeDef rawType = ctx.resolve(this.raw);

        if (raw.equals(that.raw)) {
            int numGenericParams = rawType.genericInfos.length;
            if (genericArgs.length != numGenericParams || that.genericArgs.length != numGenericParams)
                throw new RuntimeException(String.format(
                        "wrong number of generic args for %s seen when comparing %s to %s",
                        raw, this, td));
            for (int i = 0; i < numGenericParams; ++i) {
                FullTypeDesc thisGenArg = genericArgs[i], thatGenArg = that.genericArgs[i];
                switch (rawType.genericInfos[i].var) {
                    case COVARIANT:
                        if (!thisGenArg.isSubtype(thatGenArg, ctx))
                            return false;
                        break;
                    case NONVARIANT:
                        if (!thisGenArg.equals(thatGenArg))
                            return false;
                        break;
                    case CONTRAVARIANT:
                        if (!thisGenArg.isSupertype(thatGenArg, ctx))
                            return false;
                        break;
                }
            }
            return true;
        } else {
            for (FullTypeDesc superDesc : rawType.supers)
                if (superDesc.withTypeGenerics(genericArgs).isSubtype(that, ctx))
                    return true;
            return false;
        }
    }

    public boolean isSupertype(FullTypeDesc td, GlobalRCtx ctx) {
        return td.isSubtype(this, ctx);
    }

    @Override
    public boolean equals(Object o) {
        try {
            NormalFullTypeDesc that = (NormalFullTypeDesc) o;
            return raw.equals(that.raw) && Arrays.equals(genericArgs, that.genericArgs);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return raw.hashCode() * 31 + Arrays.hashCode(genericArgs);
    }

    public String toString() {
        return String.format("%s%s", raw,
                genericArgs.length == 0 ? "" : Arrays.toString(genericArgs));
    }
}

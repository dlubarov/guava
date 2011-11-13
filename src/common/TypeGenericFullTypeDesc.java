package common;

import rctx.CodeRCtx;
import rst.*;

public class TypeGenericFullTypeDesc extends GenericFullTypeDesc {
    // TODO: Can we get rid of owner? Would fix hack in MethodContext.
    // See also MethodGenericFullTypeDesc.
    public final RawTypeDesc owner;
    public final int index;

    public TypeGenericFullTypeDesc(RawTypeDesc owner, int index) {
        this.owner = owner;
        this.index = index;
    }

    public FullTypeDesc withTypeGenerics(FullTypeDesc[] typeGenerics) {
        try {
            return typeGenerics[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("too few generic args for " + owner);
        }
    }

    public FullTypeDesc withMethodGenerics(FullTypeDesc[] methodGenerics) {
        return this;
    }

    public boolean isSubtype(FullTypeDesc that, CodeRCtx ctx) {
        TypeDef type = ctx.resolve(owner);
        GenericInfo info = type.genericInfos[index];
        for (FullTypeDesc par : info.parentTypes)
            if (par.isSubtype(that, ctx))
                return true;
        return equals(that);
    }

    public boolean isSupertype(FullTypeDesc that, CodeRCtx ctx) {
        TypeDef type = ctx.resolve(owner);
        GenericInfo info = type.genericInfos[index];
        for (FullTypeDesc child : info.childTypes)
            if (child.isSupertype(that, ctx))
                return true;
        return equals(that);
    }

    @Override
    public boolean equals(Object o) {
        try {
            TypeGenericFullTypeDesc that = (TypeGenericFullTypeDesc) o;
            return owner.equals(that.owner) && index == that.index;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return owner.hashCode() * 31 + index;
    }

    public String toString() {
        return String.format("T%d", index);
    }
}

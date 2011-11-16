package common;

import rctx.*;
import rst.*;

public class TypeGenericFullTypeDesc extends GenericFullTypeDesc {
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

    public FullTypeDesc withGenerics(FullTypeDesc[] typeGenerics, FullTypeDesc[] methodGenerics) {
        try {
            return typeGenerics[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("too few generic args for " + owner);
        }
    }

    public boolean isSubtype(FullTypeDesc that, GlobalRCtx ctx) {
        TypeDef type = ctx.resolve(owner);
        GenericInfo info = type.genericInfos[index];
        for (FullTypeDesc par : info.parentTypes)
            if (par.isSubtype(that, ctx))
                return true;
        return equals(that);
    }

    public boolean isSupertype(FullTypeDesc that, GlobalRCtx ctx) {
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
        return String.format("%s:T%d", owner.name, index);
    }
}

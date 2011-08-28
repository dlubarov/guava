package common;

import rctx.CodeRCtx;

public class MethodGenericFullTypeDesc extends FullTypeDesc {
    private final RawMethodDesc owner;
    private final int index;

    public MethodGenericFullTypeDesc(RawMethodDesc owner, int index) {
        this.owner = owner;
        this.index = index;
    }

    public FullTypeDesc withTypeGenerics(FullTypeDesc[] typeGenerics) {
        return this;
    }

    public FullTypeDesc withMethodGenerics(FullTypeDesc[] methodGenerics) {
        try {
            return methodGenerics[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("too few generic args for " + owner);
        }
    }

    public boolean isSubtype(FullTypeDesc that, CodeRCtx ctx) {
        // TODO: generic upper bounds
        return equals(that);
    }

    public boolean isSupertype(FullTypeDesc that, CodeRCtx ctx) {
        // TODO: generic lower bounds
        return equals(that);
    }

    @Override
    public boolean equals(Object o) {
        try {
            MethodGenericFullTypeDesc that = (MethodGenericFullTypeDesc) o;
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
        return String.format("M%d", index);
    }
}

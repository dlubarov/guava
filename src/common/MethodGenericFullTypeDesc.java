package common;

import java.util.Arrays;

import rctx.*;

public class MethodGenericFullTypeDesc extends FullTypeDesc {
    public final RawMethodDesc owner;
    public final int index;

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
            throw new RuntimeException(String.format(
                    "too few generic args for %s; given %s",
                    owner, Arrays.toString(methodGenerics)));
        }
    }

    public FullTypeDesc withGenerics(FullTypeDesc[] typeGenerics, FullTypeDesc[] methodGenerics) {
        try {
            return methodGenerics[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException(String.format(
                    "too few generic args for %s; given %s",
                    owner, Arrays.toString(methodGenerics)));
        }
    }

    public boolean isSubtype(FullTypeDesc that, GlobalRCtx ctx) {
        // TODO: generic upper bounds
        if (that.equals(new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))))
            return true;
        return equals(that);
    }

    public boolean isSupertype(FullTypeDesc that, GlobalRCtx ctx) {
        // TODO: generic lower bounds
        if (equals(new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))))
            return true;
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
        return String.format("%s.%s:M%d", owner.owner.name, owner.name, index);
    }
}

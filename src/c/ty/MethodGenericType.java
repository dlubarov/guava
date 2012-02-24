package c.ty;

import c.*;

public class MethodGenericType extends Type {
    public final int index;

    public MethodGenericType(int index) {
        this.index = index;
    }

    @Override
    public Type withGenericArgs(Type[] typeGenericArgs, Type[] methodGenericArgs) {
        return methodGenericArgs[index];
    }

    @Override
    public Type[] getSupertypes(TypeDef typeCtx, MethodDef methodCtx) {
        return methodCtx.genericInfos[index].subOf;
    }

    @Override
    public Type[] getSubtypes(TypeDef typeCtx, MethodDef methodCtx) {
        return methodCtx.genericInfos[index].supOf;
    }

    @Override
    public d.ty.desc.TypeDesc refine() {
        return new d.ty.desc.MethodGenericTypeDesc(index);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MethodGenericType) && index == ((MethodGenericType) o).index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("M%d", index);
    }
}

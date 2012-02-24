package c.ty;

import c.*;

public class TypeGenericType extends Type {
    public final int index;

    public TypeGenericType(int index) {
        this.index = index;
    }

    @Override
    public Type withGenericArgs(Type[] typeGenericArgs, Type[] methodGenericArgs) {
        return typeGenericArgs[index];
    }

    @Override
    public Type[] getSupertypes(TypeDef typeCtx, MethodDef methodCtx) {
        return typeCtx.genericInfos[index].subOf;
    }

    @Override
    public Type[] getSubtypes(TypeDef typeCtx, MethodDef methodCtx) {
        return typeCtx.genericInfos[index].supOf;
    }

    @Override
    public d.ty.desc.TypeDesc refine() {
        return new d.ty.desc.TypeGenericTypeDesc(index);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof TypeGenericType) && index == ((TypeGenericType) o).index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("T%d", index);
    }
}

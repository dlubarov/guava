package vm.ty;

import vm.*;

public class TypeGenericType extends FullType {
    public final int index;

    public TypeGenericType(int index) {
        this.index = index;
    }

    public ConcreteType toConcrete(ZObject obj, Method meth, ConcreteType[] methodGenericArgs) {
        return obj.type.genericArgs[index];
    }

    @Override
    public boolean equals(Object o) {
        try {
            return index == ((TypeGenericType) o).index;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return index;
    }
}

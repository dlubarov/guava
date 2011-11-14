package vm.ty;

import vm.*;

public class MethodGenericType extends FullType {
    public final int index;

    public MethodGenericType(int index) {
        this.index = index;
    }

    public ConcreteType toConcrete(ZObject obj, Method meth, ConcreteType[] methodGenericArgs) {
        return methodGenericArgs[index];
    }

    @Override
    public boolean equals(Object o) {
        try {
            return index == ((MethodGenericType) o).index;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return index;
    }
}

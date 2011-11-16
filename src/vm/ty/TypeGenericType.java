package vm.ty;

import vm.*;

public class TypeGenericType extends FullType {
    public final int ownerIndex;
    public final int genericIndex;

    public TypeGenericType(int ownerIndex, int genericIndex) {
        this.ownerIndex = ownerIndex;
        this.genericIndex = genericIndex;
    }

    public ConcreteType toConcrete(ZObject obj, Method meth, ConcreteType[] methodGenericArgs) {
        return obj.getGenericArg(meth.typeTable[ownerIndex], genericIndex);
    }

    @Override
    public boolean equals(Object o) {
        try {
            TypeGenericType that = (TypeGenericType) o;
            return ownerIndex == that.ownerIndex && genericIndex == that.genericIndex;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return ownerIndex + genericIndex;
    }

    @Override public String toString() {
        return String.format("%d:%d", ownerIndex, genericIndex);
    }
}

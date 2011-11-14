package vm.ty;

import java.util.Arrays;

import vm.*;

public class ExternalType extends FullType {
    public final int tableIndex;
    public final FullType[] genericArgs;

    public ExternalType(int tableIndex, FullType[] genericArgs) {
        this.tableIndex = tableIndex;
        this.genericArgs = genericArgs;
    }

    public ConcreteType toConcrete(ZObject obj, Method meth, ConcreteType[] methodGenericArgs) {
        ConcreteType[] concreteGenericArgs = new ConcreteType[genericArgs.length];
        for (int i = 0; i < concreteGenericArgs.length; ++i)
            concreteGenericArgs[i] = genericArgs[i].toConcrete(obj, meth, methodGenericArgs);
        return new ConcreteType(meth.typeTable[tableIndex], concreteGenericArgs);
    }

    @Override
    public boolean equals(Object o) {
        try {
            ExternalType that = (ExternalType) o;
            return tableIndex == that.tableIndex && Arrays.equals(genericArgs, that.genericArgs);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return tableIndex + Arrays.hashCode(genericArgs);
    }
}

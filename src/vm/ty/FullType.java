package vm.ty;

import vm.*;

public abstract class FullType {
    public abstract ConcreteType toConcrete(ZObject obj, Method meth, ConcreteType[] methodGenericArgs);

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}

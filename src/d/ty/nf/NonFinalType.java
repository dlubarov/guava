package d.ty.nf;

import d.BaseObject;
import d.ty.ConcreteType;

/*
 * A non-final type can be a parameterized type, a type generic, or a method generic.
 * Unlike type descriptors, which store Strings, these types store references to TypeDef objects.
 * This allows them to efficiently turn themselves into concrete types when needed.
 */
public abstract class NonFinalType {
    public abstract ConcreteType toConcrete(BaseObject object, ConcreteType[] methodGenericArgs);

    @Override
    public abstract String toString();
}

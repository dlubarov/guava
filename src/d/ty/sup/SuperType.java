package d.ty.sup;

import d.ty.ConcreteType;

/*
 * A SuperType is used to describe a TypeDef's inheritance. Like NonFinalType, it stores references
 * to TypeDefs to avoid the runtime cost of String comparison. There are a couple subtle differences
 * which make NonFinalType not appropriate for describing inheritance, though:
 *     - Method-generic non-final types wouldn't make sense in this context (not a big deal).
 *     - More importantly, a type-generic non-final type stores an owner, but an owner doesn't
 *       make sense in this context, because our type-generic args always refer to the original
 *       instance's args.
 */
public abstract class SuperType {
    public abstract ConcreteType toConcrete(ConcreteType[] typeGenericArgs);

    @Override
    public abstract String toString();
}

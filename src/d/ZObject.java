package d;

import d.ty.ConcreteType;

public abstract class ZObject {
    public final ConcreteType type;

    public ZObject(ConcreteType type) {
        this.type = type;
    }

    public ConcreteType getGenericArg(TypeDef genericOwner, int index) {
        return type.rawType.superGenerics.get(genericOwner)[index].toConcrete(type.genericArgs);
    }

    public boolean isInstance(ConcreteType thatType) {
        return type.isSubtype(thatType);
    }

    @Override
    public String toString() {
        return String.format("%s@%d",
                type.toString(),
                System.identityHashCode(this));
    }
}

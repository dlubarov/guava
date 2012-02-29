package d;

import d.ty.ConcreteType;

public abstract class BaseObject {
    public final ConcreteType type;
    public final BaseObject[] fields;

    public BaseObject(ConcreteType type) {
        this.type = type;
        int numFields = type.rawType.numInstanceFields;
        if (numFields == 0)
            fields = null;
        else
            fields = new BaseObject[numFields];
    }

    public ConcreteType getGenericArg(TypeDef genericOwner, int index) {
        return type.rawType.superGenerics.get(genericOwner)[index].toConcrete(type.genericArgs);
    }

    public boolean instanceOf(ConcreteType thatType) {
        return type.isSubtype(thatType);
    }

    @Override
    public String toString() {
        return String.format("%s@%d",
                type,
                System.identityHashCode(this));
    }
}

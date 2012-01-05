package d.ty.sup;

import d.ty.ConcreteType;

public class TypeGenericSuperType extends SuperType {
    private final int index;

    public TypeGenericSuperType(int index) {
        this.index = index;
    }

    @Override
    public ConcreteType toConcrete(ConcreteType[] typeGenericArgs) {
        return typeGenericArgs[index];
    }

    @Override
    public String toString() {
        return "T" + index;
    }
}

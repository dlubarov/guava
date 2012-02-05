package d.ty.nf;

import d.*;
import d.ty.ConcreteType;

public class TypeGenericNonFinalType extends NonFinalType {
    private final TypeDef genericOwner;
    private final int index;

    public TypeGenericNonFinalType(TypeDef genericOwner, int index) {
        this.genericOwner = genericOwner;
        this.index = index;
    }

    @Override
    public ConcreteType toConcrete(BaseObject object, ConcreteType[] methodGenericArgs) {
        return object.getGenericArg(genericOwner, index);
    }

    @Override
    public String toString() {
        return genericOwner.desc + ":T" + index;
    }
}

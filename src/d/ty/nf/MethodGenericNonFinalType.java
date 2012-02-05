package d.ty.nf;

import d.BaseObject;
import d.ty.ConcreteType;

public class MethodGenericNonFinalType extends NonFinalType {
    private final int index;

    public MethodGenericNonFinalType(int index) {
        this.index = index;
    }

    @Override
    public ConcreteType toConcrete(BaseObject object, ConcreteType[] methodGenericArgs) {
        return methodGenericArgs[index];
    }

    @Override
    public String toString() {
        return "M" + index;
    }
}

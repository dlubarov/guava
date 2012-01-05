package d.ty.nf;

import d.ZObject;
import d.ty.ConcreteType;

public class MethodGenericNonFinalType extends NonFinalType {
    private final int index;

    public MethodGenericNonFinalType(int index) {
        this.index = index;
    }

    @Override
    public ConcreteType toConcrete(ZObject object, ConcreteType[] methodGenericArgs) {
        return methodGenericArgs[index];
    }

    @Override
    public String toString() {
        return "M" + index;
    }
}

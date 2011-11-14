package vm;

import vm.ty.ConcreteType;

public class NormalObject extends ZObject {
    public final ZObject[] fields;

    public NormalObject(ConcreteType type) {
        super(type);
        fields = new ZObject[((NormalType) type.rawType).numFields];
    }
}

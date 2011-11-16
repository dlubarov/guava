package vm;

import vm.ty.ConcreteType;

public class NormalObject extends ZObject {
    public final ZObject[] fields;

    public NormalObject(ConcreteType type, ZObject[] fields) {
        super(type);
        this.fields = fields;
    }

    public NormalObject(ConcreteType type) {
        this(type, new ZObject[((NormalType) type.rawType).numFields]);
    }
}

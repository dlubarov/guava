package vm;

import vm.ty.ConcreteType;

public abstract class ZObject {
    public final ConcreteType type;

    public ZObject(ConcreteType type) {
        this.type = type;
    }
}

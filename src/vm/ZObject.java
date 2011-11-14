package vm;

import vm.ty.ConcreteType;

public abstract class ZObject {
    public final ConcreteType type;

    public ZObject(ConcreteType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s@%d", type.toString(), System.identityHashCode(this));
    }
}

package vm.ty;

import vm.Type;

public class ConcreteType {
    public static final ConcreteType[] NONE = new ConcreteType[0];

    public final Type rawType;
    public final ConcreteType[] genericArgs;

    public ConcreteType(Type rawType, ConcreteType[] genericArgs) {
        this.rawType = rawType;
        this.genericArgs = genericArgs;
    }

    public ConcreteType(Type rawType) {
        this(rawType, new ConcreteType[0]);
    }
}

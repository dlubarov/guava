package vm.ty;

import vm.Type;

public class SuperCompoundType extends SuperType {
    public final Type mainType;
    public final SuperType[] genericArguments;

    public SuperCompoundType(Type mainType, SuperType[] genericArguments) {
        this.mainType = mainType;
        this.genericArguments = genericArguments;
    }

    public ConcreteType toConcrete(ConcreteType[] genericArgs) {
        ConcreteType[] args = new ConcreteType[genericArguments.length];
        for (int i = 0; i < args.length; ++i)
            args[i] = genericArguments[i].toConcrete(genericArgs);
        return new ConcreteType(mainType, genericArgs);
    }
}

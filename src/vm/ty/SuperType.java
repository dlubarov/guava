package vm.ty;

public abstract class SuperType {
    public abstract ConcreteType toConcrete(ConcreteType[] genericArgs);
}

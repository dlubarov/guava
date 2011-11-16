package vm.ty;

public class SuperGenericType extends SuperType {
    public final int index;

    public SuperGenericType(int index) {
        this.index = index;
    }

    public ConcreteType toConcrete(ConcreteType[] genericArgs) {
        return genericArgs[index];
    }
}

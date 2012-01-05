package d.ty.sup;

import d.TypeDef;
import d.ty.ConcreteType;

public class ParameterizedSuperType extends SuperType {
    private final TypeDef rawType;
    private final SuperType[] genericArgs;

    public ParameterizedSuperType(TypeDef rawType, SuperType[] genericArgs) {
        this.rawType = rawType;
        this.genericArgs = genericArgs;
    }

    @Override
    public ConcreteType toConcrete(ConcreteType[] typeGenericArgs) {
        ConcreteType[] concreteGenericArgs = new ConcreteType[genericArgs.length];
        for (int i = 0; i < concreteGenericArgs.length; ++i)
            concreteGenericArgs[i] = genericArgs[i].toConcrete(typeGenericArgs);
        return new ConcreteType(rawType, concreteGenericArgs);
    }

    @Override
    public String toString() {
        return null;
    }
}

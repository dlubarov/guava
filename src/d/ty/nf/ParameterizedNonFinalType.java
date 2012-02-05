package d.ty.nf;

import java.util.Arrays;

import d.*;
import d.ty.ConcreteType;

public class ParameterizedNonFinalType extends NonFinalType {
    private final TypeDef rawType;
    private final NonFinalType[] genericArgs;

    public ParameterizedNonFinalType(TypeDef rawType, NonFinalType[] genericArgs) {
        this.rawType = rawType;
        this.genericArgs = genericArgs;
    }

    @Override
    public ConcreteType toConcrete(BaseObject object, ConcreteType[] methodGenericArgs) {
        ConcreteType[] concreteGenericArgs = new ConcreteType[genericArgs.length];
        for (int i = 0; i < concreteGenericArgs.length; ++i)
            concreteGenericArgs[i] = genericArgs[i].toConcrete(object, methodGenericArgs);
        return new ConcreteType(rawType, concreteGenericArgs);
    }

    @Override
    public String toString() {
        return rawType.desc + Arrays.toString(genericArgs);
    }
}

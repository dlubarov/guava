package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;

public abstract class NativeTypeDef extends TypeDef {
    // TODO: get rid of numStaticFields arg, get it from associated library code.
    public NativeTypeDef(RawType desc, Variance[] genericVariances,
            int numStaticFields, String[] allInstanceFields,
            NativeMethodDef[] staticMethods, NativeMethodDef[] instanceMethods) {
        super(desc, genericVariances,
                numStaticFields, allInstanceFields,
                staticMethods, instanceMethods,
                null, null);

        // Verify that static and instance methods are correctly labeled as such.
        for (NativeMethodDef m : staticMethods)
            assert m.desc.isStatic;
        for (NativeMethodDef m : instanceMethods)
            assert !m.desc.isStatic;
    }

    @Override
    public abstract BaseObject rawInstance(ConcreteType type);
}

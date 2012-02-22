package d.nat;

import common.*;

import d.*;
import d.ty.ConcreteType;

public abstract class NativeTypeDef extends TypeDef {
    // TODO: Get rid of numStaticFields, allInstanceFields. Can infer from source.
    public NativeTypeDef(RawType desc,
            int numStaticFields, String[] allInstanceFields,
            NativeMethodDef[] staticMethods, NativeMethodDef[] instanceMethods) {
        super(desc, null,
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

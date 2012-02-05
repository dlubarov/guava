package d.nat;

import common.*;

import d.*;

public final class NativeTypeDef extends TypeDef {
    public NativeTypeDef(RawType desc, Variance[] genericVariances,
            NativeMethodDef[] staticMethods, NativeMethodDef[] instanceMethods) {
        super(desc, genericVariances, staticMethods, instanceMethods, null, null);
    }
}

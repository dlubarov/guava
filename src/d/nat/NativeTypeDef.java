package d.nat;

import common.*;

import d.*;

public abstract class NativeTypeDef extends TypeDef {
    public NativeTypeDef(RawType desc, Variance[] genericVariances,
            ConcreteMethodDef[] staticMethods, MethodDef[] instanceMethods) {
        super(desc, genericVariances, staticMethods, instanceMethods, null, null);
    }
}

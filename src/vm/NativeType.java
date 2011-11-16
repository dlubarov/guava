package vm;

import common.RawTypeDesc;

public abstract class NativeType extends Type {
    protected NativeType(RawTypeDesc desc, RawTypeDesc[] superDescs,
                         Method[] ownedMethods, int numStaticFields) {
        super(desc, superDescs, ownedMethods, null, numStaticFields);
    }

    protected NativeType(RawTypeDesc desc, Method[] ownerMethods,
                         int numStaticFields) {
        this(desc, new RawTypeDesc[] {new RawTypeDesc("core", "Object")},
                ownerMethods, numStaticFields);
    }
}

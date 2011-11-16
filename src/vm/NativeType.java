package vm;

import common.*;

public abstract class NativeType extends Type {
    protected NativeType(RawTypeDesc desc, NormalFullTypeDesc[] superDescs,
                         Method[] ownedMethods, int numStaticFields) {
        super(desc, superDescs, ownedMethods, null, numStaticFields);
    }

    protected NativeType(RawTypeDesc desc, Method[] ownerMethods,
                         int numStaticFields) {
        this(desc,
                new NormalFullTypeDesc[] {new NormalFullTypeDesc(new RawTypeDesc("core", "Object"))},
                ownerMethods, numStaticFields);
    }
}

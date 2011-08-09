package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;

import java.util.Map;

public abstract class NativeType extends Type {
    protected NativeType(RawTypeDesc desc, RawTypeDesc[] superDescs, Method[] ownedMethods,
                         Map<RawMethodDesc, RawMethodDesc> vtableDescs, int numStaticFields) {
        super(desc, superDescs, ownedMethods, vtableDescs, numStaticFields);
        for (Method m : ownedMethods)
            vtableDescs.put(m.desc, m.desc);
    }

    protected NativeType(RawTypeDesc desc, Method[] ownerMethods,
                         Map<RawMethodDesc, RawMethodDesc> vtableDescs,
                         int numStaticFields) {
        this(desc, new RawTypeDesc[] {new RawTypeDesc("core", "Object")}, ownerMethods, vtableDescs, numStaticFields);
    }
}

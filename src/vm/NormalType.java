package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;

import java.util.Map;

import vm.ty.ConcreteType;

public class NormalType extends Type {
    public final int numFields; // includes inherited fields

    public NormalType(RawTypeDesc desc, RawTypeDesc[] superDescs, Method[] ownedMethods,
                      Map<RawMethodDesc, RawMethodDesc> vtableDescs,
                      int numFields, int numStaticFields) {
        super(desc, superDescs, ownedMethods, vtableDescs, numStaticFields);
        this.numFields = numFields;
    }

    public ZObject rawInstance(ConcreteType[] genericArgs) {
        return new NormalObject(new ConcreteType(this, genericArgs));
    }
}

package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;

import java.util.Map;

public class NormalType extends Type {
    public final int numFields;

    public NormalType(RawTypeDesc desc, RawTypeDesc[] superDescs, Method[] ownedMethods,
                      Map<RawMethodDesc, RawMethodDesc> vtableDescs, int numFields) {
        super(desc, superDescs, ownedMethods, vtableDescs);
        this.numFields = numFields;
    }

    public TObject rawInstance() {
        return new NormalObject(this);
    }
}

package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;

public abstract class NativeMethod extends Method {
    protected NativeMethod(RawMethodDesc desc, RawTypeDesc[] typeDescTable, RawMethodDesc[] methodDescTable) {
        super(desc, typeDescTable, methodDescTable);
    }

    protected NativeMethod(RawMethodDesc desc, RawMethodDesc methodDescTable) {
        this(desc, new RawTypeDesc[0], new RawMethodDesc[] {methodDescTable});
    }

    protected NativeMethod(RawMethodDesc desc) {
        this(desc, new RawTypeDesc[0], new RawMethodDesc[0]);
    }
}

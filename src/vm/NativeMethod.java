package vm;

import common.RawMethodDesc;

public abstract class NativeMethod extends Method {
    protected NativeMethod(RawMethodDesc desc, RawMethodDesc[] methodDescTable) {
        super(desc, methodDescTable);
    }

    protected NativeMethod(RawMethodDesc desc, RawMethodDesc methodDescTable) {
        this(desc, new RawMethodDesc[] {methodDescTable});
    }

    protected NativeMethod(RawMethodDesc desc) {
        this(desc, new RawMethodDesc[0]);
    }
}

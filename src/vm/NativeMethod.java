package vm;

import common.RawMethodDesc;

public abstract class NativeMethod extends Method {
    protected NativeMethod(RawMethodDesc desc) {
        super(desc);
    }
}

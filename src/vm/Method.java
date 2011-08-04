package vm;

import common.RawMethodDesc;

public abstract class Method {
    public final RawMethodDesc desc;

    public Method(RawMethodDesc desc) {
        this.desc = desc;
        God.newMethod(this);
    }

    public abstract void invoke(TObject[] stack, int bp);

    public final void invoke() {
        invoke(new TObject[1000], -1);
    }
}

package vm;

import common.RawMethodDesc;

public abstract class Method {
    public final RawMethodDesc desc;
    private final RawMethodDesc[] methodDescTable;
    public Method[] methodTable;

    public Method(RawMethodDesc desc, RawMethodDesc[] methodDescTable) {
        this.desc = desc;
        this.methodDescTable = methodDescTable;
        God.newMethod(this);
    }

    public void link() {
        for (int i = 0; i < methodTable.length; ++i)
            methodTable[i] = God.resolveMethod(methodDescTable[i]);
    }

    public abstract void invoke(TObject[] stack, int bp);

    public final void invoke() {
        // TODO think about stack size...
        try {
            invoke(new TObject[10000], -1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("stack size exceeded");
        }
    }
}

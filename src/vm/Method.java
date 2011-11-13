package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;

public abstract class Method {
    public final RawMethodDesc desc;

    private final RawTypeDesc[] typeDescTable;
    protected Type[] typeTable;

    private final RawMethodDesc[] methodDescTable;
    public Method[] methodTable;

    public Method(RawMethodDesc desc, RawTypeDesc[] typeDescTable, RawMethodDesc[] methodDescTable) {
        this.desc = desc;
        this.typeDescTable = typeDescTable;
        this.methodDescTable = methodDescTable;
        God.newMethod(this);
    }

    public void link() {
        typeTable = new Type[typeDescTable.length];
        for (int i = 0; i < typeTable.length; ++i)
            typeTable[i] = God.resolveType(typeDescTable[i]);

        methodTable = new Method[methodDescTable.length];
        for (int i = 0; i < methodTable.length; ++i)
            methodTable[i] = God.resolveMethod(methodDescTable[i]);
    }

    public abstract void invoke(ZObject[] stack, int bp);

    public final void invoke() {
        // TODO think about stack size...
        try {
            invoke(new ZObject[10000], -1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("stack size might have been exceeded?", e);
        }
    }

    @Override
    public String toString() {
        return desc.toString();
    }
}

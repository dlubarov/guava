package d;

import common.*;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;
import d.ty.nf.NonFinalType;

public abstract class ConcreteMethodDef extends MethodDef {
    private static final int STACK_SIZE = 10000;

    public RawType[] rawTypeDescTable;
    public final TypeDef[] rawTypeTable;

    public TypeDesc[] fullTypeDescTable;
    public final NonFinalType[] fullTypeTable;

    public RawMethod[] methodDescTable;
    public final MethodDef[] methodTable;

    public ConcreteMethodDef(RawMethod desc,
            RawType[] typeDescTable,
            TypeDesc[] fullTypeDescTable,
            RawMethod[] methodDescTable) {
        super(desc);
        this.rawTypeDescTable = typeDescTable;
        rawTypeTable = new TypeDef[typeDescTable.length];
        this.fullTypeDescTable = fullTypeDescTable;
        fullTypeTable = new NonFinalType[fullTypeDescTable.length];
        this.methodDescTable = methodDescTable;
        methodTable = new MethodDef[methodDescTable.length];
    }

    @Override
    public void link() {
        TypeDef owner = God.resolveType(desc.owner);
        for (int i = 0; i < rawTypeTable.length; ++i)
            rawTypeTable[i] = God.resolveType(rawTypeDescTable[i]);
        for (int i = 0; i < fullTypeTable.length; ++i)
            fullTypeTable[i] = fullTypeDescTable[i].toNonFinal(owner);
        for (int i = 0; i < methodTable.length; ++i)
            methodTable[i] = God.resolveMethod(methodDescTable[i]);
    }

    public abstract void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs);

    public final void invoke(BaseObject[] initialStack, ConcreteType[] genericArgs) {
        BaseObject[] stack = new BaseObject[STACK_SIZE];
        System.arraycopy(initialStack, 0, stack, 0, initialStack.length);
        invoke(stack, -1, genericArgs);
    }

    public final void invoke(ConcreteType[] genericArgs) {
        invoke(new BaseObject[0], genericArgs);
    }
}

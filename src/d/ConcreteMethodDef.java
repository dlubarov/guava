package d;

import java.util.*;

import common.*;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;
import d.ty.nf.NonFinalType;

public abstract class ConcreteMethodDef extends MethodDef {
    private RawType[] rawTypeDescTable;
    public final TypeDef[] rawTypeTable;

    public TypeDesc[] fullTypeDescTable;
    public final NonFinalType[] fullTypeTable;

    private RawMethod[] methodDescTable;
    public final MethodDef[] methodTable;

    public ConcreteMethodDef(RawMethod desc,
            RawType[] typeDescTable,
            TypeDesc[] fullTypeDescTable,
            RawMethod[] methodDescTable,
            Map<RawMethod, RawMethod> vDescTable) {
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
        rawTypeDescTable = null;
        fullTypeDescTable = null;
        methodDescTable = null;
    }

    public abstract void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs);

    public final void invoke(ConcreteType[] genericArgs) {
        try {
            invoke(new BaseObject[1000], -1, genericArgs);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("possible stack overflow", e);
        }
    }
}

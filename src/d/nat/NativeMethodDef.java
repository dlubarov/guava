package d.nat;

import common.*;
import d.*;
import d.ty.desc.TypeDesc;

public abstract class NativeMethodDef extends ConcreteMethodDef {
    public NativeMethodDef(RawMethod desc,
            RawType[] typeDescTable, TypeDesc[] fullTypeDescTable, RawMethod[] methodDescTable) {
        super(desc, typeDescTable, fullTypeDescTable, methodDescTable);
    }

    public NativeMethodDef(RawMethod desc) {
        this(desc, RawType.NONE, TypeDesc.NONE, RawMethod.NONE);
    }
}

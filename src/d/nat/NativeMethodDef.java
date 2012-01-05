package d.nat;

import java.util.Map;

import common.*;
import d.*;
import d.ty.desc.TypeDesc;

public abstract class NativeMethodDef extends ConcreteMethodDef {
    public NativeMethodDef(RawMethod desc,
            RawType[] typeDescTable, TypeDesc[] fullTypeDescTable, RawMethod[] methodDescTable,
            Map<RawMethod, RawMethod> vDescTable) {
        super(desc, typeDescTable, fullTypeDescTable, methodDescTable, vDescTable);
    }
}

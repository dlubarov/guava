package d;

import java.util.*;

import common.*;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;
import d.ty.sup.*;

public class TypeDef {
    public final RawType desc;
    public final Variance[] genericVariances;

    public final BaseObject[] staticFields;
    public final int numInstanceFields;

    public final ConcreteMethodDef[] staticMethods;
    public final MethodDef[] instanceMethods;

    public Map<RawMethod, RawMethod> virtualMethodDescTable;
    public final Map<MethodDef, ConcreteMethodDef> virtualMethodTable;

    public Map<RawType, TypeDesc[]> superGenericDescs;
    public final Map<TypeDef, SuperType[]> superGenerics;

    public TypeDef(RawType desc, Variance[] genericVariances,
            int numStaticFields, int numInstanceFields,
            ConcreteMethodDef[] staticMethods, MethodDef[] instanceMethods,
            Map<RawMethod, RawMethod> virtualMethodDescTable,
            Map<RawType, TypeDesc[]> superGenericDescs) {
        this.desc = desc;
        this.genericVariances = genericVariances;

        this.staticFields = new BaseObject[numStaticFields];
        this.numInstanceFields = numInstanceFields;

        this.staticMethods = staticMethods;
        this.instanceMethods = instanceMethods;

        this.virtualMethodDescTable = virtualMethodDescTable;
        virtualMethodTable = new HashMap<MethodDef, ConcreteMethodDef>();

        this.superGenericDescs = superGenericDescs;
        superGenerics = new HashMap<TypeDef, SuperType[]>();

        God.newType(this);
    }

    public void link() {
        for (MethodDef method : staticMethods)
            method.link();
        for (MethodDef method : instanceMethods)
            method.link();
        for (RawMethod aDesc : virtualMethodDescTable.keySet()) {
            RawMethod bDesc = virtualMethodDescTable.get(aDesc);
            MethodDef a = God.resolveMethod(aDesc), b = God.resolveMethod(bDesc);
            if (!(b instanceof ConcreteMethodDef))
                throw new RuntimeException("vtable points to abstract method " + b);
            virtualMethodTable.put(a, (ConcreteMethodDef) b);
        }
        for (RawType superDesc : superGenericDescs.keySet()) {
            TypeDesc[] superArgDescs = superGenericDescs.get(superDesc);
            TypeDef superType = God.resolveType(superDesc);
            SuperType[] superArgs = new SuperType[superArgDescs.length];
            for (int i = 0; i < superArgs.length; ++i)
                superArgs[i] = superArgDescs[i].toSuper();
            superGenerics.put(superType, superArgs);
        }
        virtualMethodDescTable = null;
        superGenericDescs = null;
    }

    public BaseObject rawInstance(ConcreteType type) {
        return new NormalObject(type);
    }

    @Override
    public String toString() {
        // TODO: might want to list members
        return desc.toString();
    }
}

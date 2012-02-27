package d;

import java.util.*;

import util.StringUtils;

import common.*;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;
import d.ty.sup.*;

public class TypeDef {
    public final RawType desc;
    public Variance[] genericVariances;

    public final BaseObject[] staticFields;
    public final int numInstanceFields;

    public ConcreteMethodDef[] staticMethods;
    public MethodDef[] instanceMethods;

    public final IdentityHashMap<String, Integer> virtualFieldTable;

    public Map<RawMethod, RawMethod> virtualMethodDescTable;
    public final Map<MethodDef, ConcreteMethodDef> virtualMethodTable;

    public Map<RawType, TypeDesc[]> superGenericDescs;
    public final Map<TypeDef, SuperType[]> superGenerics;

    // 0 = not initialized, 1 = in progress, 2 = initialized
    public int initializationStatus = 0;

    private ConcreteType typeNoGenerics = null;

    public TypeDef(RawType desc,
            Variance[] genericVariances,
            int numStaticFields,
            String[] allInstanceFields,
            ConcreteMethodDef[] staticMethods,
            MethodDef[] instanceMethods,
            Map<RawMethod, RawMethod> virtualMethodDescTable,
            Map<RawType, TypeDesc[]> superGenericDescs) {
        this.desc = desc;
        this.genericVariances = genericVariances;

        this.staticFields = new BaseObject[numStaticFields];
        numInstanceFields = allInstanceFields.length;

        this.staticMethods = staticMethods;
        this.instanceMethods = instanceMethods;

        this.virtualFieldTable = new IdentityHashMap<String, Integer>();
        for (int i = 0; i < numInstanceFields; ++i)
            virtualFieldTable.put(allInstanceFields[i].intern(), i);

        this.virtualMethodDescTable = virtualMethodDescTable;
        virtualMethodTable = new HashMap<MethodDef, ConcreteMethodDef>();

        this.superGenericDescs = superGenericDescs;
        superGenerics = new HashMap<TypeDef, SuperType[]>();

        if (genericVariances == null || genericVariances.length == 0)
            typeNoGenerics = new ConcreteType(this);

        God.newType(this);
    }

    public void link() {
        for (MethodDef method : staticMethods)
            method.link();
        for (MethodDef method : instanceMethods)
            method.link();

        // Unless this is an abstract type, populate the virtual method table.
        if (virtualMethodDescTable != null)
            for (RawMethod aDesc : virtualMethodDescTable.keySet()) {
                RawMethod bDesc = virtualMethodDescTable.get(aDesc);
                MethodDef a = God.resolveMethod(aDesc), b = God.resolveMethod(bDesc);
                if (!(b instanceof ConcreteMethodDef))
                    throw new RuntimeException("vtable points to abstract method " + b);
                virtualMethodTable.put(a, (ConcreteMethodDef) b);
            }

        // Populate the supertype generics table.
        for (RawType superDesc : superGenericDescs.keySet()) {
            TypeDesc[] superArgDescs = superGenericDescs.get(superDesc);
            TypeDef supertype = God.resolveType(superDesc);
            SuperType[] superArgs = new SuperType[superArgDescs.length];
            for (int i = 0; i < superArgs.length; ++i)
                superArgs[i] = superArgDescs[i].toSuper();
            superGenerics.put(supertype, superArgs);
        }
    }

    public void init() {
        if (initializationStatus == 2)
            return;
        if (initializationStatus == 1)
            throw new NiftyException("Circular dependency exists in static initializers.");

        initializationStatus = 1;
        for (ConcreteMethodDef m : staticMethods)
            if (m.desc.equals(new RawMethod(true, desc, "init", 0, TypeDesc.NONE)))
                m.invoke(ConcreteType.NONE);
        initializationStatus = 2;
    }

    public BaseObject rawInstance(ConcreteType type) {
        return new NormalObject(type);
    }

    public BaseObject rawInstanceNoGenerics() {
        assert this.genericVariances.length == 0;
        return new NormalObject(typeNoGenerics);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("type ").append(desc).append(" {");

        // Unless abstract, append the virtual method descriptor table.
        if (virtualMethodDescTable != null) {
            sb.append("\n    Virtual method desc table:\n");
            for (RawMethod a : virtualMethodDescTable.keySet()) {
                RawMethod b = virtualMethodDescTable.get(a);
                sb.append(String.format("        %s -> %s\n", a, b));
            }
        }

        // Append methods.
        for (MethodDef[] ms : new MethodDef[][] {staticMethods, instanceMethods})
            for (MethodDef m : ms)
                sb.append('\n').append(StringUtils.indent(m)).append('\n');

        sb.append('}');
        return sb.toString();
    }
}

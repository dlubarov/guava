package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;

import java.util.HashMap;
import java.util.Map;

import vm.ty.ConcreteType;

public abstract class Type {
    public final RawTypeDesc desc;
    private final RawTypeDesc[] superDescs;
    private final Type[] superTypes;
    public final Method[] ownedMethods;
    public Map<RawMethodDesc, RawMethodDesc> vtableDescs;
    public Map<Method, Method> vtable;
    public ZObject[] staticFields;

    public Type(RawTypeDesc desc,
                RawTypeDesc[] superDescs,
                Method[] ownedMethods,
                Map<RawMethodDesc, RawMethodDesc> vtableDescs,
                int numStaticFields) {
        this.desc = desc;
        this.superDescs = superDescs;
        this.superTypes = new Type[superDescs.length];
        this.ownedMethods = ownedMethods;
        this.vtableDescs = vtableDescs;
        vtable = null;
        staticFields = new ZObject[numStaticFields];
        God.newType(this);
    }

    public void link() {
        for (int i = 0; i < superDescs.length; ++i)
            superTypes[i] = God.resolveType(superDescs[i]);

        for (Method m : ownedMethods)
            m.link();

        if (vtableDescs != null) { // if not abstract
            vtable = new HashMap<Method, Method>();
            for (Map.Entry<RawMethodDesc, RawMethodDesc> e : vtableDescs.entrySet())
                vtable.put(God.resolveMethod(e.getKey()), God.resolveMethod(e.getValue()));
        }
    }

    public abstract ZObject rawInstance(ConcreteType[] genericArgs);

    public String toString() {
        return desc.toString();
    }
}

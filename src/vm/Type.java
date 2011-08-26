package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;

import java.util.HashMap;
import java.util.Map;

public abstract class Type {
    public final RawTypeDesc desc;
    private final RawTypeDesc[] superDescs;
    protected final Method[] ownedMethods;
    protected final Map<RawMethodDesc, RawMethodDesc> vtableDescs;
    public Map<Method, Method> vtable;
    public ZObject[] staticFields;

    public Type(RawTypeDesc desc,
                RawTypeDesc[] superDescs,
                Method[] ownedMethods,
                Map<RawMethodDesc, RawMethodDesc> vtableDescs,
                int numStaticFields) {
        this.desc = desc;
        this.superDescs = superDescs;
        this.ownedMethods = ownedMethods;
        this.vtableDescs = vtableDescs;
        vtable = null;
        staticFields = new ZObject[numStaticFields];
        God.newType(this);
    }

    public void link() {
        vtable = new HashMap<Method, Method>();
        for (Map.Entry<RawMethodDesc, RawMethodDesc> e : vtableDescs.entrySet())
            vtable.put(God.resolveMethod(e.getKey()), God.resolveMethod(e.getValue()));

        for (Method m : ownedMethods)
            m.link();
    }

    public abstract ZObject rawInstance();
}

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
    private boolean linked = false;

    public Type(RawTypeDesc desc, RawTypeDesc[] superDescs, Method[] ownedMethods, Map<RawMethodDesc, RawMethodDesc> vtableDescs) {
        this.desc = desc;
        this.superDescs = superDescs;
        this.ownedMethods = ownedMethods;
        this.vtableDescs = vtableDescs;
        vtable = null;
        God.newType(this);
    }

    public void link() {
        if (linked)
            return;
        for (RawTypeDesc sup : superDescs)
            God.resolveType(sup).link();
        
        vtable = new HashMap<Method, Method>();
        for (Map.Entry<RawMethodDesc, RawMethodDesc> e : vtableDescs.entrySet())
            vtable.put(God.resolveMethod(e.getKey()), God.resolveMethod(e.getValue()));

        linked = true;
    }

    public abstract TObject rawInstance();
}

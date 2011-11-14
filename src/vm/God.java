package vm;

import common.RawMethodDesc;
import common.RawTypeDesc;
import vm.nat.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public final class God {
    private God() {}

    private static final Map<RawTypeDesc, Type> loadedTypes;
    private static final Map<RawMethodDesc, Method> loadedMethods;

    public static final NativeType[] nativeTypes() {
        return new NativeType[] {NatObject.TYPE, NatInt.TYPE, NatBool.TYPE, NatChar.TYPE,
                NatMutableArray.TYPE, InternalIO.TYPE};
    };

    static {
        loadedTypes = new HashMap<RawTypeDesc, Type>();
        loadedMethods = new HashMap<RawMethodDesc, Method>();
    }

    public static void newType(Type type) {
        loadedTypes.put(type.desc, type);
    }

    public static void newMethod(Method meth) {
        loadedMethods.put(meth.desc, meth);
    }

    public static Type resolveType(RawTypeDesc desc) {
        Type result = loadedTypes.get(desc);
        if (result == null)
            throw new NoSuchElementException("no such type: " + desc);
        return result;
    }

    public static Method resolveMethod(RawMethodDesc desc) {
        Method result = loadedMethods.get(desc);
        if (result == null)
            //throw new NoSuchElementException("no such method: " + desc);
            loadedMethods.put(desc, result = new AbstractMethod(desc));
        return result;
    }

    public static void linkAll(Type[] types) {
        for (Type type : nativeTypes())
            type.link();
        for (Type type : types)
            type.link();
    }
}

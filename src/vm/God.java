package vm;

import common.*;
import vm.nat.*;
import vm.ty.ConcreteType;

import java.util.*;

public final class God {
    private God() {}

    private static final Map<RawTypeDesc, Type> loadedTypes;
    private static final Map<RawMethodDesc, Method> loadedMethods;

    private static Type stringType; // stored here for easy, efficient access from native code

    public static final NativeType[] nativeTypes() {
        return new NativeType[] {NatObject.TYPE, NatInt.TYPE, NatBool.TYPE, NatChar.TYPE,
                NatMutableArray.TYPE, Console.TYPE};
    };

    static {
        loadedTypes = new HashMap<RawTypeDesc, Type>();
        loadedMethods = new HashMap<RawMethodDesc, Method>();
    }

    public static void newType(Type type) {
        loadedTypes.put(type.desc, type);
        if (type.desc.equals(new RawTypeDesc("core", "String")))
            stringType = type;
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
        assert desc != null;
        Method result = loadedMethods.get(desc);
        if (result == null)
            //throw new NoSuchElementException("no such method: " + desc);
            loadedMethods.put(desc, result = new AbstractMethod(desc));
        return result;
    }

    public static void initialize() {
        // Link all types
        for (Type type : loadedTypes.values())
            type.link();

        // Run static initializers
        for (Type type : loadedTypes.values())
            for (Method m : type.ownedMethods)
                if (m.desc.equals(new RawMethodDesc(type.desc, "init", 0, FullTypeDesc.NONE, true)))
                    m.invoke(ConcreteType.NONE);
    }

    // Convert a Java string into a Zepto string. Here for convenience.
    public static ZObject makeString(String s) {
        NatChar[] chars = new NatChar[s.length()];
        for (int i = 0; i < chars.length; ++i)
            chars[i] = new NatChar(s.charAt(i));
        NatMutableArray charArray = new NatMutableArray(
                new ConcreteType[] {new ConcreteType(NatChar.TYPE)},
                chars);
        return new NormalObject(
                new ConcreteType(stringType),
                new ZObject[] {charArray});
    }
}

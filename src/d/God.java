package d;

import java.util.*;

import common.*;
import d.nat.*;

public final class God {
    private God() {}

    private static final Map<RawType, TypeDef> allTypes;
    private static final Map<RawMethod, MethodDef> allMethods;

    private static final Set<NativeTypeDef> nativeTypes;

    static {
        allTypes = new HashMap<RawType, TypeDef>();
        allMethods = new HashMap<RawMethod, MethodDef>();
        nativeTypes = new HashSet<NativeTypeDef>();

        // We need to ensure that NativeInt, NativeBool etc. are loaded, so that their TYPE
        // objects will be created and eventually added to nativeTypes. We might as well just
        // add them manually, as it guarantees initialization and redundant add's won't hurt.
        nativeTypes.add(NativeObject.TOP_TYPE);
        nativeTypes.add(NativeBool.TYPE);
        nativeTypes.add(NativeInt.TYPE);
        nativeTypes.add(NativeDouble.TYPE);
        nativeTypes.add(NativeChar.TYPE);
        nativeTypes.add(NativeMutableArray.TYPE);
        nativeTypes.add(NativeConsole.TYPE);
    }

    public static TypeDef resolveType(RawType desc) {
        TypeDef result = allTypes.get(desc);
        if (result == null)
            throw new RuntimeException("failed to resolve " + desc);
        return result;
    }

    public static void newType(TypeDef type) {
        if (type instanceof NativeTypeDef)
            nativeTypes.add((NativeTypeDef) type);
        allTypes.put(type.desc, type);
    }

    public static MethodDef resolveMethod(RawMethod desc) {
        MethodDef result = allMethods.get(desc);
        if (result == null)
            throw new RuntimeException("failed to resolve " + desc);
        return result;
    }

    public static void newMethod(MethodDef method) {
        allMethods.put(method.desc, method);
    }
}

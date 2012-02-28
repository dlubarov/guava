package d;

import java.util.*;

import common.*;
import d.nat.*;

public final class God {
    private God() {}

    private static final Map<RawType, TypeDef> allTypes;
    private static final Map<RawMethod, MethodDef> allMethods;

    private static final Set<NativeTypeDef> nativeTypes;

    public static BaseObject objUnit, objLT, objGT, objEQ;

    public static boolean initializationComplete = false;

    static {
        allTypes = new HashMap<RawType, TypeDef>();
        allMethods = new HashMap<RawMethod, MethodDef>();
        nativeTypes = new HashSet<NativeTypeDef>();

        // We need to ensure that NativeInt, NativeBool etc. are loaded, so that their TYPE
        // objects will be created and eventually added to nativeTypes. We might as well just
        // add them manually, as it guarantees initialization and redundant add's won't hurt.
        NativeTypeDef[] nativeTypesArr = {
                NativeObject.TOP_TYPE,
                NativeBool.TYPE,
                NativeInt.TYPE,
                NativeDouble.TYPE,
                NativeChar.TYPE,
                NativeArray.TYPE,
                NativeConsole.TYPE
        };
        for (NativeTypeDef nativeType : nativeTypesArr) {
            nativeTypes.add(nativeType);
            allTypes.put(nativeType.desc, nativeType);
        }
    }

    public static boolean hasType(RawType desc) {
        return allTypes.containsKey(desc);
    }

    public static TypeDef resolveType(RawType desc) {
        TypeDef result = allTypes.get(desc);
        if (result == null)
            throw new NoSuchElementException("Failed to resolve " + desc);
        return result;
    }

    public static void newType(TypeDef type) {
        // Register the new type.
        allTypes.put(type.desc, type);
        if (type instanceof NativeTypeDef)
            nativeTypes.add((NativeTypeDef) type);
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

    public static void linkAll() {
        for (TypeDef typeDef : allTypes.values())
            typeDef.link();
    }

    public static void runStaticInitializers() {
        assert !initializationComplete;
        for (TypeDef typeDef : allTypes.values()) {
            typeDef.init();

            if (typeDef.desc.equals(RawType.coreUnit)) {
                // TODO: Fragile...
                objUnit = typeDef.staticFields[0];
            }
            if (typeDef.desc.equals(RawType.coreRelation)) {
                // TODO: Fragile...
                objLT = typeDef.staticFields[0];
                objGT = typeDef.staticFields[1];
                objEQ = typeDef.staticFields[2];
            }
        }
        initializationComplete = true;
    }
}

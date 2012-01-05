package c;

import java.util.*;

import c.ty.*;

public final class TypeUtils {
    private TypeUtils() {}

    public static Type union(Type... types) {
        // TODO: temporary stupid union, fails when it doesn't have to
        // Should search for all common supertypes, then
        //     - if there are none, fail
        //     - if there are multiple, return the intersection
        //       (if they share no common subtype, fail)
        if (types.length == 0)
            return new ParameterizedType("core", "Bottom");
        Type result = types[0];
        for (Type type : types)
            if (!type.equals(result))
                throw new RuntimeException("sorry, my dumb code can only union identical types");
        return result;
    }

    public static Type intersectionNoBottom(Type[] types, TypeDef typeCtx, MethodDef methodCtx) {
        search:
        for (Type possibleCommonSubtype : types) {
            for (Type otherType : types)
                if (!possibleCommonSubtype.isSubtype(otherType, typeCtx, methodCtx))
                    continue search;
            return possibleCommonSubtype;
        }
        throw new IllegalArgumentException("no common subtype for " + Arrays.toString(types));
    }
}

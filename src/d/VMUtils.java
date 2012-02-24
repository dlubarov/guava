package d;

import common.RawType;
import d.nat.*;
import d.ty.ConcreteType;

public final class VMUtils {
    private VMUtils() {}

    // TODO: The get* methods are slow and fragile.
    // Should store direct references to the needed objects.

    public static BaseObject getUnit() {
        // TODO: getUnit is very slow, and fragile.
        // Should store a direct reference to Unit.singleton.
        return God.resolveType(RawType.coreUnit).staticFields[0];
    }

    public static BaseObject getLT() {
        return God.resolveType(RawType.coreRelation).staticFields[0];
    }

    public static BaseObject getGT() {
        return God.resolveType(RawType.coreRelation).staticFields[1];
    }

    public static BaseObject getEQ() {
        return God.resolveType(RawType.coreRelation).staticFields[2];
    }

    public static BaseObject makeString(String javaString) {
        BaseObject[] chars = new BaseObject[javaString.length()];
        for (int i = 0; i < chars.length; ++i)
            chars[i] = new NativeChar(javaString.charAt(i));
        ConcreteType arrayType = new ConcreteType(
                NativeMutableArray.TYPE,
                new ConcreteType[] {
                        new ConcreteType(God.resolveType(RawType.coreChar))
                });
        BaseObject charArray = new NativeMutableArray(arrayType, chars);
        TypeDef stringTypeDef = God.resolveType(RawType.coreString);
        NormalObject guavaString = new NormalObject(new ConcreteType(stringTypeDef));
        guavaString.fields[0] = charArray;
        return guavaString;
    }

    public static String extractString(BaseObject guavaString) {
        BaseObject[] chars = ((NativeMutableArray) guavaString.fields[0]).contents;
        StringBuilder sb = new StringBuilder();
        for (BaseObject c : chars)
            sb.append(((NativeChar) c).value);
        return sb.toString();
    }
}

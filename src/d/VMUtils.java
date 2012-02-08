package d;

import common.RawType;
import d.nat.*;
import d.ty.ConcreteType;

public final class VMUtils {
    private VMUtils() {}

    public static BaseObject getUnit() {
        // TODO: getUnit is very slow, should store a direct reference to Unit.singleton.
        return God.resolveType(RawType.coreUnit).staticFields[0];
    }

    public static BaseObject makeString(String javaString) {
        BaseObject[] chars = new BaseObject[javaString.length()];
        for (int i = 0; i < chars.length; ++i)
            chars[i] = new NativeChar(javaString.charAt(i));
        BaseObject charArray = new NativeMutableArray(chars);
        TypeDef stringTypeDef = God.resolveType(RawType.coreString);
        NormalObject zeptoString = new NormalObject(new ConcreteType(stringTypeDef));
        zeptoString.fields[0] = charArray;
        return zeptoString;
    }

    public static String extractString(BaseObject zeptoString) {
        BaseObject[] chars = ((NativeMutableArray) zeptoString.fields[0]).contents;
        StringBuilder sb = new StringBuilder();
        for (BaseObject c : chars)
            sb.append(((NativeChar) c).value);
        return sb.toString();
    }
}

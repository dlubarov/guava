package d;

import common.RawType;
import d.nat.*;
import d.ty.ConcreteType;

public final class VMUtils {
    private VMUtils() {}

    public static BaseObject getVoid() {
        return God.resolveType(RawType.coreVoid).staticFields[0];
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

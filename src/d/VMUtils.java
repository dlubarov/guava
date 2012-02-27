package d;

import common.RawType;
import d.nat.*;
import d.ty.ConcreteType;

public final class VMUtils {
    private VMUtils() {}

    public static BaseObject makeString(String javaString) {
        BaseObject[] chars = new BaseObject[javaString.length()];
        for (int i = 0; i < chars.length; ++i)
            chars[i] = new NativeChar(javaString.charAt(i));
        ConcreteType arrayType = new ConcreteType(
                NativeArray.TYPE,
                new ConcreteType[] {
                        new ConcreteType(God.resolveType(RawType.coreChar))
                });
        BaseObject charArray = new NativeArray(arrayType, chars);
        TypeDef stringTypeDef = God.resolveType(RawType.coreString);
        NormalObject guavaString = new NormalObject(new ConcreteType(stringTypeDef));
        guavaString.fields[0] = charArray;
        return guavaString;
    }

    public static String extractString(BaseObject guavaString) {
        BaseObject[] chars = ((NativeArray) guavaString.fields[0]).contents;
        StringBuilder sb = new StringBuilder();
        for (BaseObject c : chars)
            sb.append(((NativeChar) c).value);
        return sb.toString();
    }
}

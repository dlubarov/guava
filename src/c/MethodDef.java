package c;

import java.util.Arrays;

import common.*;

import c.ty.Type;
import c.gen.MethodGenericInfo;
import c.stm.Block;

public class MethodDef {
    public final RawType owner;

    public final MethodVisibility visibility;
    public final boolean isStatic, isSealed;

    public final Type returnType;
    public final String name;
    public final MethodGenericInfo[] genericInfos;
    public final Type[] paramTypes;
    public final Block body;

    public MethodDef(RawType owner,
            MethodVisibility visibility, boolean isStatic, boolean isSealed,
            Type returnType, String name, MethodGenericInfo[] genericInfos, Type[] paramTypes,
            Block body) {
        this.owner = owner;
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isSealed = isSealed;
        this.returnType = returnType;
        this.name = name;
        this.genericInfos = genericInfos;
        this.paramTypes = paramTypes;
        this.body = body;
    }

    private String qualsString() {
        StringBuilder sb = new StringBuilder();
        if (isStatic)
            sb.append("static ");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format(
                "%s%s %s%s%s%s",
                qualsString(),
                returnType,
                name,
                genericInfos.length == 0
                        ? ""
                        : Arrays.toString(genericInfos),
                paramTypes,
                body == null
                        ? ";"
                        : " " + body);
    }
}

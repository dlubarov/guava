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
    public final String[] paramNames;
    public final Block body;

    public MethodDef(RawType owner,
            MethodVisibility visibility, boolean isStatic, boolean isSealed,
            Type returnType, String name, MethodGenericInfo[] genericInfos,
            Type[] paramTypes, String[] paramNames, Block body) {
        this.owner = owner;
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isSealed = isSealed;
        this.returnType = returnType;
        this.name = name;
        this.genericInfos = genericInfos;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
        this.body = body;
    }

    private String qualsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(visibility);
        if (isStatic)
            sb.append("static ");
        if (isSealed)
            sb.append("sealed ");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(qualsString());
        sb.append(returnType).append(' ').append(name);

        // Append generic infos.
        if (genericInfos.length > 0)
            sb.append(Arrays.toString(genericInfos));

        // Append parameter list.
        sb.append('(');
        for (int i = 0; i < paramNames.length; ++i) {
            if (i > 0)
                sb.append(", ");
            sb.append(paramTypes[i]).append(' ').append(paramNames[i]);
        }
        sb.append(')');

        // Append the method body.
        if (body == null)
            sb.append(';');
        else
            sb.append(' ').append(body);

        return sb.toString();
    }
}

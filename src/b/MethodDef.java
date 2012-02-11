package b;

import java.util.Arrays;

import c.gen.MethodGenericInfo;

import common.*;

import b.gen.MethodGenericParam;
import b.stm.Block;

public class MethodDef {
    public final MethodVisibility visibility;
    public final boolean isStatic, isSealed;
    public final Type returnType;
    public final String name;
    public final MethodGenericParam[] genericParams;
    public final String[] paramNames;
    public final Type[] paramTypes;
    public final Block body;

    public MethodDef(MethodVisibility visibility, boolean isStatic, boolean isSealed,
            Type returnType, String name, MethodGenericParam[] genericParams,
            Type[] paramTypes, String[] paramNames, Block body) {
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isSealed = isSealed;
        this.returnType = returnType;
        this.name = name;
        this.genericParams = genericParams;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
        this.body = body;

        if (isStatic && body == null)
            throw new RuntimeException("static methods can't be abstract");
        if (isSealed && body == null)
            throw new RuntimeException("sealed methods can't be abstract");
    }

    public int genericParamIndex(String genericParam) {
        for (int i = 0; i < genericParams.length; ++i)
            if (genericParams[i].name.equals(genericParam))
                return i;
        return -1;
    }

    public c.MethodDef refine(TypeDef typeCtx) {
        // Validity checks.
        if (body == null && !typeCtx.isAbstract)
            throw new NiftyException("Non-abstract type '%s' has abstract method '%s'", typeCtx.desc, name);

        // Refine generic parameters.
        MethodGenericInfo[] refinedGenerics = new MethodGenericInfo[genericParams.length];
        for (int i = 0; i < refinedGenerics.length; ++i)
            refinedGenerics[i] = genericParams[i].refine(typeCtx, this, i);

        // Refine parameter types.
        c.ty.Type[] refinedParamTypes = Type.refineAll(paramTypes, typeCtx, this);

        return new c.MethodDef(
                typeCtx.desc,
                visibility, isStatic, isSealed,
                returnType.refine(typeCtx, this),
                name,
                refinedGenerics,
                refinedParamTypes,
                paramNames,
                body == null ? null : body.refine(typeCtx, this));
    }

    private String qualsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(visibility);
        if (isStatic)
            sb.append("static ");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(qualsString());
        sb.append(returnType).append(' ').append(name);

        // Append generic parameter list.
        if (genericParams.length > 0)
            sb.append(Arrays.toString(genericParams));

        // Append parameter list.
        sb.append('(');
        for (int i = 0; i < paramNames.length; ++i) {
            if (i > 0)
                sb.append(", ");
            sb.append(paramTypes[i]).append(' ').append(paramNames[i]);
        }
        sb.append(')');

        // Append method body.
        if (body == null)
            sb.append(';');
        else
            sb.append(' ').append(body);

        return sb.toString();
    }
}

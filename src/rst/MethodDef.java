package rst;

import common.FullTypeDesc;
import common.RawMethodDesc;
import rst.stm.BlockStm;

import static util.StringUtils.implode;

public class MethodDef {
    public final String name;
    public final FullTypeDesc retType;
    public final int numGenericParams;
    public final FullTypeDesc[] paramTypes;
    public final BlockStm body;
    public final boolean isStatic, isSealed;
    public RawMethodDesc desc;

    public MethodDef(String name, FullTypeDesc retType, int numGenericParams, FullTypeDesc[] paramTypes,
                     BlockStm body, boolean isStatic, boolean isSealed) {
        this.name = name;
        this.retType = retType;
        this.numGenericParams = numGenericParams;
        this.paramTypes = paramTypes;
        this.body = body;
        this.isStatic = isStatic;
        this.isSealed = isSealed;
    }

    public String toString() {
        // TODO: sloppy toString
        StringBuilder sb = new StringBuilder();
        if (isStatic)
            sb.append("static ");
        
        sb.append(retType).append(' ').append(name);

        if (numGenericParams > 0)
            sb.append('[').append(numGenericParams).append(']');

        sb.append('(').append(implode(", ", paramTypes)).append(')');

        if (body == null)
            sb.append(';');
        else
            sb.append(' ').append(body);

        return sb.toString();
    }
}

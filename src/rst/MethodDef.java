package rst;

import common.FullTypeDesc;
import common.RawMethodDesc;
import rctx.*;
import rst.stm.*;
import vm.NormalMethod;

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
    
    public NormalMethod compile(MethodRCtx ctx) {
        CompilationResult result = body.compile(new CodeRCtx(ctx));
        return new NormalMethod(desc,
                ctx.getTypeTable(), ctx.getMethodTable(),
                ctx.numLocals(), result.code.getCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isStatic)
            sb.append("static ");
        if (isSealed)
            sb.append("sealed ");
        
        sb.append(retType).append(' ').append(name);

        if (numGenericParams > 0) {
            sb.append("[T0");
            for (int i = 1; i < numGenericParams; ++i)
                sb.append(", T").append(i);
            sb.append(']');
        }
        
        sb.append('(');
        for (int i = 0; i < paramTypes.length; ++i) {
            if (i != 0)
                sb.append(", ");
            sb.append(paramTypes[i]);
            sb.append(" local_");
            sb.append(isStatic? i : i + 1);
        }
        sb.append(')');

        if (body == null)
            sb.append(';');
        else
            sb.append(' ').append(body);

        return sb.toString();
    }
}

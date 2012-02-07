package b.stm;

import b.*;
import b.exp.Expression;

public class LocalDef extends Statement {
    public final Type type;
    public final String[] names;
    public final Expression[] initVals;

    public LocalDef(Type type, String[] names, Expression[] initVals) {
        this.type = type;
        this.names = names;
        this.initVals = initVals;
    }

    @Override
    public c.stm.Statement refine(TypeDef typeCtx, MethodDef methodCtx) {
        c.exp.Expression[] refinedInitVals = new c.exp.Expression[initVals.length];
        for (int i = 0; i < refinedInitVals.length; ++i)
            refinedInitVals[i] = initVals[i] == null
                    ? null
                    : initVals[i].refine(typeCtx, methodCtx);
        return new c.stm.LocalDef(type.refine(typeCtx, methodCtx), names, refinedInitVals);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(' ');
        for (int i = 0; i < names.length; ++i) {
            if (i > 0)
                sb.append(", ");
            sb.append(names[i]);
            if (initVals[i] != null)
                sb.append(" = ").append(initVals[i]);
        }
        sb.append(';');
        return sb.toString();
    }
}

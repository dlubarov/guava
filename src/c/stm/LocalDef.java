package c.stm;

import c.exp.Expression;
import c.ty.Type;

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

package a.stm;

import a.Type;
import a.exp.Expression;

public class LocalDef extends Statement {
    public final Type type;
    public final String[] names;
    public final Expression[] initVals;

    public LocalDef(Type type, String[] names, Expression[] initVals) {
        this.type = type;
        this.names = names;
        this.initVals = initVals;
    }

    public LocalDef(Type type, String name, Expression initVal) {
        this(type, new String[] {name}, new Expression[] {initVal});
    }

    @Override
    public b.stm.Statement refine() {
        b.exp.Expression[] refinedInitVals = new b.exp.Expression[initVals.length];
        for (int i = 0; i < refinedInitVals.length; ++i)
            if (initVals[i] != null)
                refinedInitVals[i] = initVals[i].refine();
        return new b.stm.LocalDef(type.refine(), names, refinedInitVals);
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

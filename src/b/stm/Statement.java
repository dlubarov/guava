package b.stm;

import b.*;

public abstract class Statement {
    public abstract c.stm.Statement refine(TypeDef typeCtx, MethodDef methodCtx);

    public static c.stm.Statement[] refineAll(Statement[] statements, TypeDef typeCtx, MethodDef methodCtx) {
        c.stm.Statement[] result = new c.stm.Statement[statements.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = statements[i].refine(typeCtx, methodCtx);
        return result;
    }

    @Override
    public abstract String toString();
}

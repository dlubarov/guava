package b.exp;

import b.*;

public abstract class Expression {
    public abstract c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx);

    @Override
    public abstract String toString();
}

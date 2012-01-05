package b.stm;

import b.*;

public abstract class Statement {
    public abstract c.stm.Statement refine(TypeDef typeCtx, MethodDef methodCtx);

    @Override
    public abstract String toString();
}

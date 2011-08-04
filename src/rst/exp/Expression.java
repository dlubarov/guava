package rst.exp;

import common.FullTypeDesc;
import rctx.CodeRCtx;

public abstract class Expression {
    public abstract String toString();

    public abstract FullTypeDesc inferType(CodeRCtx ctx);

    public boolean evalMayHaveSideEffects() {
        return true;
    }
}

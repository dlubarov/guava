package rst.exp;

import common.FullTypeDesc;
import rctx.CodeRCtx;

public class LocalGet extends Expression {
    private final int index;

    public LocalGet(int index) {
        this.index = index;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return ctx.localType(index);
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return String.format("local_%d", index);
    }
}

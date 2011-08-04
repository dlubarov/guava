package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import common.RawTypeDesc;
import rctx.CodeRCtx;

public class LitInt extends Expression {
    private final int value;

    public LitInt(int value) {
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return new NormalFullTypeDesc(new RawTypeDesc("core", "Int"));
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return Integer.toString(value);
    }
}

package rst.exp;

import common.FullTypeDesc;
import rctx.CodeRCtx;

public class LocalSet extends Expression {
    private final int index;
    private final Expression value;

    public LocalSet(int index, Expression value) {
        this.index = index;
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return value.inferType(ctx);
    }

    public String toString() {
        return String.format("(local_%d = %s)", index, value);
    }
}

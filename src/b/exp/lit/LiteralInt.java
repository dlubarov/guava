package b.exp.lit;

import b.*;
import b.exp.Expression;

public class LiteralInt extends Expression {
    public final int value;

    public LiteralInt(int value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.lit.LiteralInt(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

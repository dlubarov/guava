package b.exp.lit;

import b.*;
import b.exp.Expression;

public class LiteralLong extends Expression {
    public final long value;

    public LiteralLong(long value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.lit.LiteralLong(value);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}

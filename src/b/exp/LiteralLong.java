package b.exp;

import b.*;

public class LiteralLong extends Expression {
    public final long value;

    public LiteralLong(long value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.LiteralLong(value);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}

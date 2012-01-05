package b.exp;

import b.*;

public class LiteralInt extends Expression {
    public final int value;

    public LiteralInt(int value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.LiteralInt(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

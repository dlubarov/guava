package b.exp.lit;

import b.*;
import b.exp.Expression;

public class LiteralBool extends Expression {
    public final boolean value;

    public LiteralBool(boolean value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.lit.LiteralBool(value);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}

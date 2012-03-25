package b.exp.lit;

import b.*;
import b.exp.Expression;

public class LiteralDouble extends Expression {
    public final double value;

    public LiteralDouble(double value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.lit.LiteralDouble(value);
    }

    @Override
    public String toString() {
        return Double.toHexString(value);
    }
}

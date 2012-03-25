package b.exp.lit;

import b.*;
import b.exp.Expression;

public class LiteralString extends Expression {
    public final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.lit.LiteralString(value);
    }

    @Override
    public String toString() {
        return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"") + '"';
    }
}

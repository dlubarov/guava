package b.exp;

import b.*;

public class LiteralString extends Expression {
    public final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.LiteralString(value);
    }

    @Override
    public String toString() {
        return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"") + '"';
    }
}

package b.exp.lit;

import b.*;
import b.exp.Expression;

public class LiteralChar extends Expression {
    public final char value;

    public LiteralChar(char value) {
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.lit.LiteralChar(value);
    }

    @Override
    public String toString() {
        return String.format("'%c'", value == '\''? "\\'" : value);
    }
}

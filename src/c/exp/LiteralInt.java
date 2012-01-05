package c.exp;

import c.CodeContext;
import c.ty.*;

public class LiteralInt extends Expression {
    public final int value;

    public LiteralInt(int value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType("core", "Int");
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

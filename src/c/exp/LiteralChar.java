package c.exp;

import c.CodeContext;
import c.ty.*;

public class LiteralChar extends Expression {
    public final char value;

    public LiteralChar(char value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType("core", "Char");
    }

    @Override
    public String toString() {
        return Character.toString(value);
    }
}

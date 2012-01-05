package c.exp;

import c.CodeContext;
import c.ty.*;

public class LiteralString extends Expression {
    public final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType("core", "String");
    }

    @Override
    public String toString() {
        return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"") + '"';
    }
}

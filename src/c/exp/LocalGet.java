package c.exp;

import c.CodeContext;
import c.ty.Type;

public class LocalGet extends Expression {
    public final String name;

    public LocalGet(String name) {
        this.name = name;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return ctx.getLocalType(name);
    }

    @Override
    public String toString() {
        return name;
    }
}

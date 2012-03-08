package c.exp;

import c.*;
import c.ty.Type;
import d.Opcodes;

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
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        return new CodeTree(Opcodes.GET_LOCAL, ctx.getLocalIndex(name));
    }

    @Override
    public String toString() {
        return name;
    }
}

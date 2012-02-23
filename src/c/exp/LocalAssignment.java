package c.exp;

import c.*;
import c.ty.Type;
import d.Opcodes;

public class LocalAssignment extends Expression {
    public final String name;
    public final Expression value;

    public LocalAssignment(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new LocalGet(name).inferType(ctx);
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        return new CodeTree(
                value.compile(ctx),
                Opcodes.DUP,
                Opcodes.PUT_LOCAL,
                ctx.getLocalIndex(name)
        );
    }

    @Override
    public String toString() {
        return String.format("%s = %s", name, value);
    }
}

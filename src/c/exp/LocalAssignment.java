package c.exp;

import common.NiftyException;

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
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        Type expectedType = ctx.getLocalType(name);
        if (!value.hasType(expectedType, ctx))
            throw new NiftyException("'%s' does not conform to %s's type of %s.",
                    value, name, expectedType);
        return new CodeTree(
                value.compile(expectedType, ctx),
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

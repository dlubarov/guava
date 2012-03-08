package c.exp;

import common.NiftyException;

import c.*;
import c.ty.*;
import d.Opcodes;

public class InstanceFieldAssignment extends Expression {
    public final Expression target;
    public final String fieldName;
    public final Expression value;

    public InstanceFieldAssignment(Expression target, String fieldName, Expression value) {
        this.target = target;
        this.fieldName = fieldName;
        this.value = value;
    }

    private FieldDef getField(CodeContext ctx) {
        // Piggyback on our friend, InstanceFieldGet.
        return new InstanceFieldGet(target, fieldName).getField(ctx);
    }

    @Override
    public Type inferType(CodeContext ctx) {
        // Piggyback on our friend, InstanceFieldGet.
        return new InstanceFieldGet(target, fieldName).inferType(ctx);
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        FieldDef field = getField(ctx);
        ParameterizedType targetAsFieldOwner = target.inferType(ctx).asSupertype(field.owner, ctx);

        Type expectedType = field.type;
        expectedType = expectedType.withGenericArgs(targetAsFieldOwner.genericArgs, null);
        if (!value.hasType(expectedType, ctx))
            throw new NiftyException("'%s' does not conform to %s.%s's type of %s.",
                    value, field.owner, field.name, expectedType);

        int fieldNameIndex = ctx.method.getStringTableIndex(fieldName);
        return new CodeTree(
                value.compile(expectedType, ctx),
                Opcodes.DUP,
                target.compile(ctx),
                Opcodes.PUT_INSTANCE_FIELD,
                fieldNameIndex
        );
    }

    @Override
    public String toString() {
        return String.format("%s.%s = %s", target, fieldName, value);
    }
}

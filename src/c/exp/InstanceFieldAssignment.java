package c.exp;

import c.*;
import c.ty.Type;
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
    public CodeTree compile(CodeContext ctx) {
        getField(ctx); // Ensure that the field exists.
        int fieldNameIndex = ctx.method.getStringTableIndex(fieldName);
        return new CodeTree(
                target.compile(ctx),
                value.compile(ctx),
                Opcodes.PUT_INSTANCE_FIELD,
                fieldNameIndex);
    }

    @Override
    public String toString() {
        return String.format("%s.%s = %s", target, fieldName, value);
    }
}

package c.exp;

import c.*;
import c.ty.Type;
import common.RawType;
import d.Opcodes;

public class StaticFieldAssignment extends Expression {
    public final RawType owner;
    public final String fieldName;
    public final Expression value;

    public StaticFieldAssignment(RawType owner, String fieldName, Expression value) {
        this.owner = owner;
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return ctx.project.resolve(owner).getStaticField(fieldName).type;
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        TypeDef ownerDef = ctx.project.resolve(owner);
        int fieldIndex = ownerDef.getStaticFieldIndex(fieldName);
        return new CodeTree(
                value.compile(ctx),
                Opcodes.PUT_STATIC_FIELD,
                fieldIndex);
    }

    @Override
    public String toString() {
        return String.format("%s.%s = %s", owner, fieldName, value);
    }
}

package c.exp;

import c.*;
import c.ty.Type;
import common.RawType;
import d.Opcodes;

public class StaticFieldGet extends Expression {
    public final RawType owner;
    public final String fieldName;

    public StaticFieldGet(RawType owner, String fieldName) {
        this.owner = owner;
        this.fieldName = fieldName;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return ctx.project.resolve(owner).getStaticField(fieldName).type;
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        TypeDef ownerDef = ctx.project.resolve(owner);
        int fieldIndex = ownerDef.getStaticFieldIndex(fieldName);
        return new CodeTree(Opcodes.GET_STATIC_FIELD, fieldIndex);
    }

    @Override
    public String toString() {
        return String.format("%s.%s", owner, fieldName);
    }
}

package c.exp;

import c.CodeContext;
import c.ty.Type;
import common.RawType;

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
    public String toString() {
        return String.format("%s.%s = %s", owner, fieldName, value);
    }
}

package c.exp;

import c.CodeContext;
import c.ty.Type;
import common.RawType;

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
    public String toString() {
        return String.format("%s.%s", owner, fieldName);
    }
}

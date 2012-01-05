package c.exp;

import c.CodeContext;
import c.ty.Type;

public class InstanceFieldAssignment extends Expression {
    public final Expression target;
    public final String fieldName;
    public final Expression value;

    public InstanceFieldAssignment(Expression target, String fieldName, Expression value) {
        this.target = target;
        this.fieldName = fieldName;
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new InstanceFieldGet(target, fieldName).inferType(ctx);
    }

    @Override
    public String toString() {
        return String.format("%s.%s = %s", target, fieldName, value);
    }
}

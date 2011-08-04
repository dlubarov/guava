package rst.exp;

import common.FullTypeDesc;
import rctx.CodeRCtx;

public class InstanceFieldSet extends Expression {
    private final Expression target;
    private final String fieldName;
    private final Expression value;

    public InstanceFieldSet(Expression target, String fieldName, Expression value) {
        this.target = target;
        this.fieldName = fieldName;
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        // Other languages derive the type of an assignment from the destination, but using the source allows code like
        // Number n; Integer x, y; x = n = y;
        // I don't see any problems with this...
        return value.inferType(ctx);
    }

    public String toString() {
        return String.format("%s.%s = %s", target, fieldName, value);
    }
}

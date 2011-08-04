package rst.exp;

import common.FullTypeDesc;
import common.RawTypeDesc;
import rctx.CodeRCtx;

public class ClassFieldSet extends Expression {
    private final RawTypeDesc target;
    private final String fieldName;
    private final Expression value;

    public ClassFieldSet(RawTypeDesc target, String fieldName, Expression value) {
        this.target = target;
        this.fieldName = fieldName;
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return value.inferType(ctx);
    }

    public String toString() {
        return String.format("%s.%s = %s", target, fieldName, value);
    }
}

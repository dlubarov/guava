package rst.exp;

import common.FullTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

public class LocalSet extends Expression {
    private final int index;
    private final Expression value;

    public LocalSet(int index, Expression value) {
        this.index = index;
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return value.inferType(ctx);
    }

    public CodeTree compile(CodeRCtx ctx) {
        FullTypeDesc valueType = value.inferType(ctx), expectedType = ctx.getLocalType(index);
        if (!valueType.isSubtype(expectedType, ctx.methodCtx.globalCtx))
            throw new RuntimeException(String.format(
                    "rval (%s) has type %s, does not match local type %s",
                    value, valueType, expectedType));

        return new CodeTree(value.compile(ctx), Opcodes.DUP, Opcodes.PUT_LOCAL, index);
    }

    public String toString() {
        return String.format("(local_%d = %s)", index, value);
    }
}

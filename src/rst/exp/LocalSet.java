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
        return new CodeTree(value.compile(ctx), Opcodes.DUP, Opcodes.PUT_LOCAL, index);
    }

    public String toString() {
        return String.format("(local_%d = %s)", index, value);
    }
}

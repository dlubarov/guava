package rst.exp;

import common.FullTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

public class LocalGet extends Expression {
    private final int index;

    public LocalGet(int index) {
        this.index = index;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return ctx.getLocalType(index);
    }

    public CodeTree compile(CodeRCtx ctx) {
        return new CodeTree(Opcodes.GET_LOCAL, index);
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return String.format("local_%d", index);
    }
}

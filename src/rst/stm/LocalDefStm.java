package rst.stm;

import common.FullTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import rst.exp.Expression;
import vm.Opcodes;

public class LocalDefStm extends Statement {
    private final FullTypeDesc type;
    private final int index;
    private final Expression initVal;

    public LocalDefStm(FullTypeDesc type, int index, Expression initVal) {
        this.type = type;
        this.index = index;
        this.initVal = initVal;
    }

    public CompilationResult compile(CodeRCtx ctx) {
        return new CompilationResult(
                new CodeTree(initVal.compile(ctx), Opcodes.PUT_LOCAL, index),
                ctx.addLocal(index, type));
    }

    public String toString() {
        if (initVal == null)
            return String.format("%s local_%d;", type, index);
        return String.format("%s local_%d = %s;", type, index, initVal);
    }
}

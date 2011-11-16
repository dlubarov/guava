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
        CodeRCtx newCtx = ctx.addLocal(index, type);

        if (initVal == null)
            return new CompilationResult(new CodeTree(), newCtx);

        FullTypeDesc initValType = initVal.inferType(ctx);
        if (!initValType.isSubtype(type, ctx.methodCtx.globalCtx))
            throw new RuntimeException(String.format("%s has type %s, not %s", initVal, initValType, type));
        return new CompilationResult(
                new CodeTree(initVal.compile(ctx), Opcodes.PUT_LOCAL, index),
                newCtx);
    }

    public String toString() {
        if (initVal == null)
            return String.format("%s local_%d;", type, index);
        return String.format("%s local_%d = %s;", type, index, initVal);
    }
}

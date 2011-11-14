package rst.exp;

import common.FullTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

public abstract class Expression {
    public abstract String toString();

    public abstract FullTypeDesc inferType(CodeRCtx ctx);

    public abstract CodeTree compile(CodeRCtx ctx);

    public CodeTree compileNoResult(CodeRCtx ctx) {
        return new CodeTree(compile(ctx), Opcodes.POP);
    }

    public boolean evalMayHaveSideEffects() {
        return true;
    }
}

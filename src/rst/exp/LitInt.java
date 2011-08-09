package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import common.RawTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

public class LitInt extends Expression {
    private final int value;

    public LitInt(int value) {
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return new NormalFullTypeDesc(new RawTypeDesc("core", "Int"));
    }

    public CodeTree compile(CodeRCtx ctx) {
        return new CodeTree(Opcodes.CONST_INT, value);
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return Integer.toString(value);
    }
}

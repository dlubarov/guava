package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import common.RawTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

public class LitBool extends Expression {
    private final boolean value;

    public static final LitBool TRUE = new LitBool(true), FALSE = new LitBool(false);

    private LitBool(boolean value) {
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return new NormalFullTypeDesc(new RawTypeDesc("core", "Bool"));
    }

    public CodeTree compile(CodeRCtx ctx) {
        return new CodeTree(value? Opcodes.CONST_TRUE : Opcodes.CONST_FALSE);
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return Boolean.toString(value);
    }
}

package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import common.RawTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

public class LitString extends Expression {
    private final String value;

    public LitString(String value) {
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return new NormalFullTypeDesc(new RawTypeDesc("core", "String"));
    }

    public CodeTree compile(CodeRCtx ctx) {
        return new CodeTree(Opcodes.CONST_STRING, ctx.getStringIndex(value));
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return '"' + value + '"';
    }
}

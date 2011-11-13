package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import common.RawTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

public class LitChar extends Expression {
    private final char value;

    public LitChar(char value) {
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return new NormalFullTypeDesc(new RawTypeDesc("core", "Char"));
    }

    public CodeTree compile(CodeRCtx ctx) {
        return new CodeTree(Opcodes.CONST_CHAR, (int) value);
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return Character.toString(value);
    }
}

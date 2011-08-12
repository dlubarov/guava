package rst.stm;

import comp.CodeTree;
import rctx.CodeRCtx;
import rst.exp.Expression;
import vm.Opcodes;

public class ReturnStm extends Statement {
    private final Expression value;

    public ReturnStm(Expression value) {
        this.value = value;
    }

    public CompilationResult compile(CodeRCtx ctx) {
        return new CompilationResult(new CodeTree(value.compile(ctx), Opcodes.RETURN), ctx);
    }

    public String toString() {
        if (value == null)
            return "return;";
        return String.format("return %s;", value);
    }
}

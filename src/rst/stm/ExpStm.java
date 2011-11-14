package rst.stm;

import rctx.CodeRCtx;
import rst.exp.Expression;

public class ExpStm extends Statement {
    private final Expression exp;

    public ExpStm(Expression exp) {
        this.exp = exp;
    }

    public CompilationResult compile(CodeRCtx ctx) {
        return new CompilationResult(exp.compileNoResult(ctx), ctx);
    }

    public String toString() {
        return exp + ";";
    }
}

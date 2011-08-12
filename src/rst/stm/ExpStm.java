package rst.stm;

import comp.CodeTree;
import rctx.CodeRCtx;
import rst.exp.Expression;

public class ExpStm extends Statement {
    private final Expression exp;

    public ExpStm(Expression exp) {
        this.exp = exp;
    }

    public CompilationResult compile(CodeRCtx ctx) {
        return new CompilationResult(exp.compile(ctx), ctx);
    }

    public String toString() {
        return exp + ";";
    }
}

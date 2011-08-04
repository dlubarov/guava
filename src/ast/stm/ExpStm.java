package ast.stm;

import ast.exp.Expression;
import ctx.CodeContext;

public class ExpStm extends Statement {
    private final Expression exp;

    public ExpStm(Expression exp) {
        this.exp = exp;
    }

    public RefineResult refine(CodeContext ctx) {
        return new RefineResult(new rst.stm.ExpStm(exp.refine(ctx)), ctx);
    }

    public String toString() {
        return exp + ";";
    }
}

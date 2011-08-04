package ast.stm;

import ast.exp.Expression;
import ctx.CodeContext;

import static util.StringUtils.indent;

public class WhileStm extends Statement {
    private final Expression cond;
    private final Statement body;

    public WhileStm(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    public RefineResult refine(CodeContext ctx) {
        return new RefineResult(new rst.stm.WhileStm(cond.refine(ctx), body.refine(ctx).stm), ctx);
    }

    public String toString() {
        return String.format("while (%s)\n%s", cond, indent(body));
    }
}

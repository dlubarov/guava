package ast.stm;

import ast.exp.Expression;
import ctx.CodeContext;

import static util.StringUtils.indent;

public class IfStm extends Statement {
    private final Expression cond;
    private final Statement body;

    public IfStm(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    public RefineResult refine(CodeContext ctx) {
        // TODO: make a separate rst.stm.IfStm for efficiency
        return new RefineResult(new IfElseStm(cond, body, EmptyStm.INST).refine(ctx).stm, ctx);
    }

    public String toString() {
        return String.format("if (%s)\n%s", cond, indent(body));
    }
}

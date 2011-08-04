package ast.stm;

import ast.exp.Expression;
import ctx.CodeContext;

import static util.StringUtils.indent;

public class IfElseStm extends Statement {
    private final Expression cond;
    private final Statement bodyTrue;
    private final Statement bodyFalse;

    public IfElseStm(Expression cond, Statement bodyTrue, Statement bodyFalse) {
        this.cond = cond;
        this.bodyTrue = bodyTrue;
        this.bodyFalse = bodyFalse;
    }

    public RefineResult refine(CodeContext ctx) {
        return new RefineResult(
                new rst.stm.IfElseStm(
                        cond.refine(ctx),
                        bodyTrue.refine(ctx).stm,
                        bodyFalse.refine(ctx).stm),
                ctx);
    }

    public String toString() {
        return String.format("if (%s)\n%s\nelse\n%s", cond, indent(bodyTrue), indent(bodyFalse));
    }
}

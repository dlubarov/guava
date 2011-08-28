package ast.stm;

import ast.TypedVar;
import ast.exp.Expression;
import ctx.CodeContext;

public class IterationStm extends Statement {
    private final TypedVar var;
    private final Expression iterable;
    private final Statement body;

    public IterationStm(TypedVar var, Expression iterable, Statement body) {
        this.var = var;
        this.iterable = iterable;
        this.body = body;
    }

    public RefineResult refine(CodeContext ctx) {
        CodeContext newCtx = ctx.addLocal(var.name);
        return new RefineResult(
                new rst.stm.IterationStm(
                        ctx.resolveFull(var.type),
                        newCtx.localIndex(var.name),
                        iterable.refine(ctx),
                        body.refine(newCtx).stm),
                ctx);
    }

    public String toString() {
        return String.format("for (%s : %s)\n%s",
                var, iterable, body);
    }
}

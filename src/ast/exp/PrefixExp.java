package ast.exp;

import ast.Type;
import ctx.CodeContext;

public class PrefixExp extends Expression {
    private final PrefixOp op;
    private final Expression inner;

    public PrefixExp(PrefixOp op, Expression inner) {
        this.op = op;
        this.inner = inner;
    }

    public rst.exp.Expression refine(CodeContext ctx) {
        // "++x" is basically sugar for x.++(), so we can delegate the work
        return new Invocation(new MemberAccess(inner, op.toString()), new Type[0], new Expression[0]).refine(ctx);
    }

    public String toString() {
        return op.toString() + inner;
    }
}

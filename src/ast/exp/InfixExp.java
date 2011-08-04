package ast.exp;

import ast.Type;
import ctx.CodeContext;

public class InfixExp extends Expression {
    private final Expression a, b;
    private final InfixOp op;

    public InfixExp(Expression a, InfixOp op, Expression b) {
        this.a = a;
        this.op = op;
        this.b = b;
    }

    public rst.exp.Expression refine(CodeContext ctx) {
        // "x + y" is basically sugar for x.+(y), so we can delegate the work
        return new Invocation(new MemberAccess(a, op.toString()), new Type[0], new Expression[] {b}).refine(ctx);
    }

    public String toString() {
        return String.format("(%s %s %s)", a, op, b);
    }
}

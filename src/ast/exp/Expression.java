package ast.exp;

import ctx.CodeContext;

public abstract class Expression {
    public abstract rst.exp.Expression refine(CodeContext ctx);

    public abstract String toString();
}

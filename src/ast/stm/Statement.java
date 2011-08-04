package ast.stm;

import ctx.CodeContext;

public abstract class Statement {
    public abstract String toString();

    public abstract RefineResult refine(CodeContext ctx);
}

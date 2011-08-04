package ast.stm;

import ast.exp.Expression;
import ctx.CodeContext;

public class ReturnStm extends Statement {
    private final Expression value;

    public ReturnStm(Expression value) {
        this.value = value;
    }

    public RefineResult refine(CodeContext ctx) {
        return new RefineResult(new rst.stm.ReturnStm(value.refine(ctx)), ctx);
    }

    public String toString() {
        if (value == null)
            return "return;";
        return String.format("return %s;", value);
    }
}

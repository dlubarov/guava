package ast.exp;

import ctx.CodeContext;
import rst.exp.LocalGet;

public class VarExp extends Expression {
    public final String id;

    public VarExp(String id) {
        this.id = id;
    }

    public rst.exp.Expression refine(CodeContext ctx) {
        return new LocalGet(ctx.localIndex(id));
    }

    public String toString() {
        return id;
    }
}

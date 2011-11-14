package ast.exp;

import ctx.CodeContext;

public class LitVoid extends Expression {
    public static final LitVoid SINGLETON = new LitVoid();

    private LitVoid() {}

    public rst.exp.Expression refine(CodeContext ctx) {
        return new MemberAccess(new VarExp("Void"), "SINGLETON").refine(ctx);
    }

    public String toString() {
        return "void";
    }
}

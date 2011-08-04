package ast.exp;

import ctx.CodeContext;

public class LitInt extends Expression {
    private final int value;

    public LitInt(int value) {
        this.value = value;
    }

    public rst.exp.LitInt refine(CodeContext ctx) {
        return new rst.exp.LitInt(value);
    }

    public String toString() {
        return Integer.toString(value);
    }
}

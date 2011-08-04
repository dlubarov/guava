package ast.exp;

import ctx.CodeContext;

public class LitBool extends Expression {
    private final boolean value;

    public static final LitBool TRUE = new LitBool(true), FALSE = new LitBool(false);

    private LitBool(boolean value) {
        this.value = value;
    }

    public rst.exp.LitBool refine(CodeContext ctx) {
        return value? rst.exp.LitBool.TRUE : rst.exp.LitBool.FALSE;
    }

    public String toString() {
        return Boolean.toString(value);
    }
}

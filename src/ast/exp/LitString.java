package ast.exp;

import ctx.CodeContext;

public class LitString extends Expression {
    private final String value;

    public LitString(String value) {
        this.value = value;
    }

    public rst.exp.LitString refine(CodeContext ctx) {
        return new rst.exp.LitString(value);
    }

    public String toString() {
        return '"' + value + '"';
    }
}

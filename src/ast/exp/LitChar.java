package ast.exp;

import ctx.CodeContext;

public class LitChar extends Expression {
    private final char value;

    public LitChar(char value) {
        this.value = value;
    }

    public rst.exp.LitChar refine(CodeContext ctx) {
        return new rst.exp.LitChar(value);
    }

    public String toString() {
        return Character.toString(value);
    }
}

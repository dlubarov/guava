package a.exp.lit;

import a.exp.Expression;

public class LiteralInt extends Expression {
    public final int value;

    public LiteralInt(int value) {
        this.value = value;
    }

    @Override
    public b.exp.Expression refine() {
        return new b.exp.lit.LiteralInt(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

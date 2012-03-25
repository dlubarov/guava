package a.exp.lit;

import a.exp.Expression;

public class LiteralDouble extends Expression {
    public final double value;

    public LiteralDouble(double value) {
        this.value = value;
    }

    @Override
    public b.exp.Expression refine() {
        return new b.exp.lit.LiteralDouble(value);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}

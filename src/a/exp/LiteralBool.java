package a.exp;

public class LiteralBool extends Expression {
    public final boolean value;

    public LiteralBool(boolean value) {
        this.value = value;
    }

    @Override
    public b.exp.Expression refine() {
        return new b.exp.LiteralBool(value);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}

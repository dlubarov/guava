package a.exp;

public class LiteralLong extends Expression {
    public final long value;

    public LiteralLong(long value) {
        this.value = value;
    }

    @Override
    public b.exp.Expression refine() {
        return new b.exp.LiteralLong(value);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}

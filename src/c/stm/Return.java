package c.stm;

import c.exp.Expression;

public class Return extends Statement {
    public final Expression value;

    public Return(Expression value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("return %s;", value);
    }
}

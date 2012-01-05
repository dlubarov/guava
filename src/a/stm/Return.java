package a.stm;

import a.exp.Expression;

public class Return extends Statement {
    public final Expression value;

    public Return(Expression value) {
        this.value = value;
    }

    @Override
    public b.stm.Statement refine() {
        return new b.stm.Return(value.refine());
    }

    @Override
    public String toString() {
        return String.format("return %s;", value);
    }
}

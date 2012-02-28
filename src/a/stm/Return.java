package a.stm;

import a.exp.*;

public class Return extends Statement {
    public final Expression value;

    public Return(Expression value) {
        this.value = value;
    }

    @Override
    public b.stm.Statement refine() {
        return new b.stm.Return(value == null ? null : value.refine());
    }

    @Override
    public String toString() {
        if (value == null)
            return "return;";
        return String.format("return %s;", value);
    }
}

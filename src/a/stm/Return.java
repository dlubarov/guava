package a.stm;

import a.Type;
import a.exp.*;

public class Return extends Statement {
    public final Expression value;

    public Return(Expression value) {
        this.value = value;
    }

    @Override
    public b.stm.Statement refine() {
        if (value == null)
            return new b.stm.Return(
                    new Invocation(
                            new Variable("Unit"),
                            Type.NONE, Expression.NONE
                    ).refine()
            );
        return new b.stm.Return(value.refine());
    }

    @Override
    public String toString() {
        if (value == null)
            return "return;";
        return String.format("return %s;", value);
    }
}

package b.stm;

import b.*;
import b.exp.Expression;

public class Return extends Statement {
    public final Expression value;

    public Return(Expression value) {
        this.value = value;
    }

    @Override
    public c.stm.Statement refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.stm.Return(value == null ? null : value.refine(typeCtx, methodCtx));
    }

    @Override
    public String toString() {
        if (value == null)
            return "return;";
        return String.format("return %s;", value);
    }
}

package b.stm;

import b.*;
import b.exp.Expression;

public class Evaluation extends Statement {
    public final Expression exp;

    public Evaluation(Expression exp) {
        this.exp = exp;
    }

    @Override
    public c.stm.Statement refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.stm.Evaluation(exp.refine(typeCtx, methodCtx));
    }

    @Override
    public String toString() {
        return exp.toString() + ";";
    }
}

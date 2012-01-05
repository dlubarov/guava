package a.stm;

import a.exp.Expression;

public class Evaluation extends Statement {
    public final Expression exp;

    public Evaluation(Expression exp) {
        this.exp = exp;
    }

    @Override
    public b.stm.Statement refine() {
        return new b.stm.Evaluation(exp.refine());
    }

    @Override
    public String toString() {
        return exp.toString() + ";";
    }
}

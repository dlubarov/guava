package c.stm;

import c.exp.Expression;

public class Evaluation extends Statement {
    public final Expression exp;

    public Evaluation(Expression exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return exp.toString() + ";";
    }
}

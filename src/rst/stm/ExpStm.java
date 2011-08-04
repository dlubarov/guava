package rst.stm;

import rst.exp.Expression;

public class ExpStm extends Statement {
    private final Expression exp;

    public ExpStm(Expression exp) {
        this.exp = exp;
    }

    public String toString() {
        return exp + ";";
    }
}

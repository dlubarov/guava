package rst.stm;

import rst.exp.Expression;

public class ReturnStm extends Statement {
    private final Expression value;

    public ReturnStm(Expression value) {
        this.value = value;
    }

    public String toString() {
        if (value == null)
            return "return;";
        return String.format("return %s;", value);
    }
}

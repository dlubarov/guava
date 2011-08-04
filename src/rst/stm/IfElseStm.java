package rst.stm;

import rst.exp.Expression;

import static util.StringUtils.indent;

public class IfElseStm extends Statement {
    private final Expression cond;
    private final Statement bodyTrue, bodyFalse;

    public IfElseStm(Expression cond, Statement bodyTrue, Statement bodyFalse) {
        this.cond = cond;
        this.bodyTrue = bodyTrue;
        this.bodyFalse = bodyFalse;
    }
    
    public String toString() {
        return String.format("if (%s)\n%s\nelse\n%s",
                cond, indent(bodyTrue), indent(bodyFalse));
    }
}

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
        StringBuilder sb = new StringBuilder("if (");
        sb.append(cond).append(')');

        if (bodyTrue instanceof BlockStm)
            sb.append(' ').append(bodyTrue);
        else
            sb.append('\n').append(indent(bodyTrue));

        sb.append("\nelse");

        if (bodyFalse instanceof BlockStm)
            sb.append(' ').append(bodyFalse);
        else
            sb.append('\n').append(indent(bodyFalse));

        return sb.toString();
    }
}

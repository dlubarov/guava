package rst.stm;

import rst.exp.Expression;

import static util.StringUtils.indent;

public class WhileStm extends Statement {
    private final Expression cond;
    private final Statement body;

    public WhileStm(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    public String toString() {
        if (body instanceof BlockStm)
            return String.format("while (%s) %s", cond, body);
        return String.format("while (%s)\n%s", cond, indent(body));
    }
}

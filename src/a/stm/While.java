package a.stm;

import static util.StringUtils.indent;
import a.exp.Expression;

public class While extends Statement {
    public final Expression cond;
    public final Statement body;

    public While(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public b.stm.Statement refine() {
        return new b.stm.While(cond.refine(), body.refine());
    }

    @Override
    public String toString() {
        if (body instanceof Block)
            return String.format("while (%s) %s", cond, body);
        return String.format("while (%s)\n%s", cond, indent(body));
    }
}

package a.stm;

import static util.StringUtils.indent;
import a.exp.Expression;

public class If extends Statement {
    public final Expression cond;
    public final Statement body;

    public If(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public b.stm.Statement refine() {
        return new IfElse(cond, body, new Block()).refine();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(cond).append(')');
        if (body instanceof Block)
            sb.append(' ').append(body);
        else
            sb.append('\n').append(indent(body));
        return sb.toString();
    }
}

package b.stm;

import static util.StringUtils.indent;
import b.*;
import b.exp.Expression;

public class While extends Statement {
    public final Expression cond;
    public final Statement body;

    public While(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public c.stm.Statement refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.stm.While(cond.refine(typeCtx, methodCtx), body.refine(typeCtx, methodCtx));
    }

    @Override
    public String toString() {
        if (body instanceof Block)
            return String.format("while (%s) %s", cond, body);
        return String.format("while (%s)\n%s", cond, indent(body));
    }
}

package b.stm;

import static util.StringUtils.indent;
import b.*;
import b.exp.Expression;

public class IfElse extends Statement {
    public final Expression cond;
    public final Statement bodyTrue, bodyFalse;

    public IfElse(Expression cond, Statement bodyTrue, Statement bodyFalse) {
        this.cond = cond;
        this.bodyTrue = bodyTrue;
        this.bodyFalse = bodyFalse;
    }

    @Override
    public c.stm.Statement refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.stm.IfElse(
                cond.refine(typeCtx, methodCtx),
                bodyTrue.refine(typeCtx, methodCtx),
                bodyFalse.refine(typeCtx, methodCtx));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(cond).append(')');
        if (bodyTrue instanceof Block)
            sb.append(' ').append(bodyTrue).append(' ');
        else
            sb.append('\n').append(indent(bodyTrue)).append('\n');

        sb.append("else");
        if (bodyFalse instanceof Block)
            sb.append(' ').append(bodyFalse);
        else if (bodyFalse instanceof IfElse)
            sb.append(' ').append(bodyFalse);
        else
            sb.append('\n').append(indent(bodyFalse));

        return sb.toString();
    }
}

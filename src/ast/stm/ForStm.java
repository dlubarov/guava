package ast.stm;

import ast.exp.Expression;
import ast.exp.LitBool;
import ctx.CodeContext;

import static util.StringUtils.indent;

public class ForStm extends Statement {
    private final Statement a;
    private final Expression b;
    private final Expression c;
    private final Statement body;

    public ForStm(Statement a, Expression b, Expression c, Statement body) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.body = body;
    }

    public RefineResult refine(CodeContext ctx) {
        // for (a; b; c) body --> {a; while (b) {body; c;}}
        Expression bb = b == null ? LitBool.TRUE : b;
        Expression cc = c == null ? LitBool.TRUE : c; // could be any exp without side effects
        return new RefineResult(
                new BlockStm(a, new WhileStm(bb, new BlockStm(body, new ExpStm(cc)))).refine(ctx).stm,
                ctx);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("for (").append(a);
        if (b != null)
            sb.append(' ').append(b);
        sb.append(';');
        if (c != null)
            sb.append(' ').append(c);
        sb.append(")\n").append(indent(body));
        return sb.toString();
    }
}

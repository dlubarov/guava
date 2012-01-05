package a.stm;

import static util.StringUtils.indent;

import a.exp.Expression;

public class For extends Statement {
    public final Statement a;
    public final Expression b;
    public final Expression c;
    public final Statement body;

    public For(Statement a, Expression b, Expression c, Statement body) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.body = body;
    }

    @Override
    public b.stm.Statement refine() {
        return new Block(
                a,
                new While(
                        b,
                        new Block(
                                body,
                                new Evaluation(c)
                        )
                )
        ).refine();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("for (").append(a);
        if (b != null)
            sb.append(' ').append(b);
        sb.append(';');
        if (c != null)
            sb.append(' ').append(c);
        sb.append(")");

        if (body instanceof Block)
            sb.append(' ').append(body);
        else
            sb.append('\n').append(indent(body));
        return sb.toString();
    }
}

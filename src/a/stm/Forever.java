package a.stm;

import static util.StringUtils.indent;
import a.exp.lit.LiteralBool;

public class Forever extends Statement {
    public final Statement body;

    public Forever(Statement body) {
        this.body = body;
    }

    @Override
    public b.stm.Statement refine() {
        return new While(new LiteralBool(true), body).refine();
    }

    @Override
    public String toString() {
        if (body instanceof Block)
            return "forever " + body;
        return "forever\n" + indent(body);
    }
}

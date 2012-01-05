package c.exp;

import static util.StringUtils.implode;
import c.CodeContext;
import c.ty.*;

public class Instantiation extends Expression {
    public final ParameterizedType type;
    public final Expression[] args;

    public Instantiation(ParameterizedType type, Expression[] args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return type;
    }

    @Override
    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}

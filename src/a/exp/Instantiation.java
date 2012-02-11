package a.exp;

import static util.StringUtils.implode;
import a.Type;

public class Instantiation extends Expression {
    public final Type type;
    public final Expression[] args;

    public Instantiation(Type type, Expression[] args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public b.exp.Expression refine() {
        b.exp.Expression[] refinedArgs = Expression.refineAll(args);
        return new b.exp.Instantiation(type.refine(), refinedArgs);
    }

    @Override
    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}

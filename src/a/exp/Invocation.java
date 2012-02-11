package a.exp;

import static util.StringUtils.implode;
import a.Type;

public class Invocation extends Expression {
    public final Expression target;
    public final Type[] genericArgs;
    public final Expression[] args;

    public Invocation(Expression target, Type[] genericArgs, Expression[] args) {
        this.target = target;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    @Override
    public b.exp.Expression refine() {
        b.Type[] refinedGenericArgs = Type.refineAll(genericArgs);
        b.exp.Expression[] refinedArgs = Expression.refineAll(args);
        return new b.exp.Invocation(target.refine(), refinedGenericArgs, refinedArgs);
    }

    @Override
    public String toString() {
        return String.format("%s%s(%s)",
                target,
                genericArgs.length == 0 ? "" : '[' + implode(", ", genericArgs) + ']',
                implode(", ", args));
    }
}

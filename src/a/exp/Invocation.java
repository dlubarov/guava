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
        // Refine generic arguments
        b.Type[] refinedGenericArgs = new b.Type[genericArgs.length];
        for (int i = 0; i < refinedGenericArgs.length; ++i)
            refinedGenericArgs[i] = genericArgs[i].refine();

        // Refine arguments
        b.exp.Expression[] refinedArgs = new b.exp.Expression[args.length];
        for (int i = 0; i < refinedArgs.length; ++i)
            refinedArgs[i] = args[i].refine();

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

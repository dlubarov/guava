package c.exp;

import static util.StringUtils.implode;
import c.ty.Type;

public class InstanceMethodInvocation extends Expression {
    public final Expression target;
    public final String methodName;
    public final Type[] genericArgs;
    public final Expression[] args;

    public InstanceMethodInvocation(Expression target, String methodName,
            Type[] genericArgs, Expression[] args) {
        this.target = target;
        this.methodName = methodName;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    // TODO: be sure to use returnType.withGenericArgs(...)

    @Override
    public String toString() {
        return String.format("%s%s(%s)",
                target,
                genericArgs.length == 0 ? "" : '[' + implode(", ", genericArgs) + ']',
                implode(", ", args));
    }
}

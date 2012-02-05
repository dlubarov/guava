package c.exp;

import static util.StringUtils.implode;
import c.CodeContext;
import c.ty.*;

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
    public Type inferType(CodeContext ctx) {
        Type targetType = target.inferType(ctx);
        ParameterizedType[] concreteSuperTypes = targetType.getConcreteSupertypes(ctx.type, ctx.method);
        assert concreteSuperTypes.length > 0 : "list of concrete supertypes should at least contain core.Top";
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s%s(%s)",
                target,
                genericArgs.length == 0 ? "" : '[' + implode(", ", genericArgs) + ']',
                implode(", ", args));
    }
}

package c.exp;

import static util.StringUtils.implode;
import c.ty.Type;

import common.RawType;

public class StaticMethodInvocation extends Expression {
    public final RawType owner;
    public final String methodName;
    public final Type[] genericArgs;
    public final Expression[] args;

    public StaticMethodInvocation(RawType owner, String methodName,
            Type[] genericArgs, Expression[] args) {
        this.owner = owner;
        this.methodName = methodName;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format("%s.%s%s(%s)",
                owner,
                methodName,
                genericArgs.length == 0 ? "" : '[' + implode(", ", genericArgs) + ']',
                implode(", ", args));
    }
}

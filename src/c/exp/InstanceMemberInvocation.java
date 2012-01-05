package c.exp;

import static util.StringUtils.implode;
import c.ty.Type;

public class InstanceMemberInvocation extends Expression {
    public final Expression target;
    public final String memberName;
    public final Type[] genericArgs;
    public final Expression[] args;

    public InstanceMemberInvocation(Expression target, String memberName,
            Type[] genericArgs, Expression[] args) {
        this.target = target;
        this.memberName = memberName;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format("%s%s(%s)",
                target,
                genericArgs.length == 0 ? "" : '[' + implode(", ", genericArgs) + ']',
                implode(", ", args));
    }
}

package c.exp;

import static util.StringUtils.implode;

import common.NiftyException;

import c.CodeContext;
import c.ty.Type;

public class InstanceMemberInvocation extends Expression {
    public final Expression target;
    public final String memberName;
    public final Type[] genericArgs;
    public final Expression[] args;

    private final InstanceMethodInvocation normalInvocation, fieldGetInvocation;

    public InstanceMemberInvocation(Expression target, String memberName,
            Type[] genericArgs, Expression[] args) {
        this.target = target;
        this.memberName = memberName;
        this.genericArgs = genericArgs;
        this.args = args;
        normalInvocation = new InstanceMethodInvocation(
                target, memberName, genericArgs, args);
        fieldGetInvocation = new InstanceMethodInvocation(
                new InstanceFieldGet(target, memberName),
                "get", genericArgs, args);
    }

    @Override
    public Type inferType(CodeContext ctx) {
        if (normalInvocation.isValid(ctx))
            return normalInvocation.inferType(ctx);
        if (fieldGetInvocation.isValid(ctx))
            return fieldGetInvocation.inferType(ctx);
        throw new NiftyException("no such method: %s.%s", target, memberName);
    }

    @Override
    public String toString() {
        return String.format("%s%s(%s)",
                target,
                genericArgs.length == 0 ? "" : '[' + implode(", ", genericArgs) + ']',
                implode(", ", args));
    }
}

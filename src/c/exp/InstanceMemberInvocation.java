package c.exp;

import static util.StringUtils.implode;

import java.util.Arrays;

import common.NiftyException;

import c.*;
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

    private InstanceMethodInvocation getActualInvocation(CodeContext ctx) {
        boolean normalIsValid = normalInvocation.isValid(ctx),
                fieldGetIsValid = fieldGetInvocation.isValid(ctx);
        if (normalIsValid && fieldGetIsValid)
            throw new NiftyException(
                    "Can't tell if '%s' is an invocation of the '%s' method or the 'get' method of the '%s' field.",
                    this, memberName, memberName);
        if (normalIsValid)
            return normalInvocation;
        if (fieldGetIsValid)
            return fieldGetInvocation;
        throw new NiftyException(
                "No such method: '%s.%s' with argument types %s. Target has type %s.",
                target, memberName,
                Arrays.toString(Expression.inferAllTypes(args, ctx)),
                target.inferType(ctx));
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return getActualInvocation(ctx).inferType(ctx);
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        return getActualInvocation(ctx).compile(ctx);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(target).append('.').append(memberName);
        if (genericArgs.length > 0)
            sb.append(Arrays.toString(genericArgs));
        sb.append('(').append(implode(", ", args)).append(')');
        return sb.toString();
    }
}

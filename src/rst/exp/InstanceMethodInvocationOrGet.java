package rst.exp;

import common.FullTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static util.StringUtils.implode;

/*
 * This awkward class exists because in the AST-to-RST transformation
 * the Invocation class can't differentiate
 *     foo.method(arg) // instance method invocation
 *     foo.field(idx) // indexing into a collection, or anything with "get"
 * Since Invocation doesn't have the information needed to differentiate,
 * the decision is postponed and this class makes the decision isntead.
 */

public class InstanceMethodInvocationOrGet extends Expression {
    private final Expression target;
    private final String memberName;
    private final FullTypeDesc[] genericArgs;
    private final Expression[] args;
    
    public InstanceMethodInvocationOrGet(Expression target, String memberName,
                                         FullTypeDesc[] genericArgs, Expression[] args) {
        this.target = target;
        this.memberName = memberName;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    private InstanceMethodInvocation normalVersion(CodeRCtx ctx) {
        return new InstanceMethodInvocation(target, memberName, genericArgs, args);
    }

    private InstanceMethodInvocation getVersion(CodeRCtx ctx) {
        return new InstanceMethodInvocation(
                    new InstanceFieldGet(target, memberName),
                    "get", genericArgs, args);
    }

    private boolean isGet(CodeRCtx ctx) {
        FullTypeDesc normalMethodResult = null, getMethodResult = null;
        try {
            normalMethodResult = normalVersion(ctx).inferType(ctx);
        } catch (RuntimeException e) {}
        try {
            getMethodResult = getVersion(ctx).inferType(ctx);
        } catch (RuntimeException e) {}

        if (normalMethodResult == null && getMethodResult == null)
            throw new NoSuchElementException(String.format("method %s not found in %s", memberName, this));
        if (normalMethodResult != null && getMethodResult != null)
            throw new RuntimeException(String.format("%s is ambiguous because field and method share a name", this));

        return normalMethodResult == null;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        if (isGet(ctx))
            return getVersion(ctx).inferType(ctx);
        return normalVersion(ctx).inferType(ctx);
    }

    public CodeTree compile(CodeRCtx ctx) {
        if (isGet(ctx))
            return getVersion(ctx).compile(ctx);
        return normalVersion(ctx).compile(ctx);
    }
    
    public String toString() {
        return String.format("%s.%s%s(%s)",
                target, memberName,
                genericArgs.length == 0? "" : Arrays.toString(genericArgs),
                implode(", ", args));
    }
}

package c.exp;

import static util.StringUtils.implode;
import c.*;
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
    public Type inferType(CodeContext ctx) {
        // TODO: could avoid inferring types for each arg, instead using hasType
        Type[] argTypes = new Type[args.length];
        for (int i = 0; i < argTypes.length; ++i)
            argTypes[i] = args[i].inferType(ctx);
        TypeDef ownerDef = ctx.project.resolve(owner);
        MethodDef meth = ownerDef.getStaticMethod(methodName, genericArgs, argTypes, ctx);
        // TODO: will need to add possible type generics once static invocation of instance methods is supported
        return meth.returnType.withGenericArgs(null, genericArgs);
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

package b.exp;

import static util.StringUtils.implode;
import c.exp.InstanceMemberInvocation;
import b.*;

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
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        c.ty.Type[] refinedGenericArgs = new c.ty.Type[genericArgs.length];
        for (int i = 0; i < refinedGenericArgs.length; ++i)
            refinedGenericArgs[i] = genericArgs[i].refine(typeCtx, methodCtx);

        c.exp.Expression[] refinedArgs = new c.exp.Expression[args.length];
        for (int i = 0; i < refinedArgs.length; ++i)
            refinedArgs[i] = args[i].refine(typeCtx, methodCtx);

        // We have to deal with these types of invocations:
        //     class get: SomeClass(...)
        //     class method: SomeClass.myMethod(...)
        //     normal instance method: something.myMethod(...)
        //     functor: someCollection(idx)

        if (target instanceof Variable) {
            String var = ((Variable) target).name;
            if (typeCtx.typeExists(var))
                // Class get: SomeClass(...)
                return new c.exp.StaticMethodInvocation(
                        typeCtx.qualifyType(var), "get", refinedGenericArgs, refinedArgs);
        }

        if (target instanceof MemberAccess) {
            MemberAccess targetMemAcc = (MemberAccess) target;
            Expression owner = targetMemAcc.target;
            if (owner instanceof Variable) {
                String ownerName = ((Variable) owner).name;
                if (typeCtx.typeExists(ownerName))
                    // Class method: SomeClass.myMethod(...)
                    return new c.exp.StaticMethodInvocation(
                            typeCtx.qualifyType(ownerName),
                            targetMemAcc.memberName,
                            refinedGenericArgs, refinedArgs);
            }
            return new InstanceMemberInvocation(
                    owner.refine(typeCtx, methodCtx),
                    targetMemAcc.memberName,
                    refinedGenericArgs, refinedArgs);
        }

        // Must be a get call, like getArray()(index)
        return new c.exp.InstanceMethodInvocation(
                target.refine(typeCtx, methodCtx), "get",
                refinedGenericArgs, refinedArgs);
    }

    @Override
    public String toString() {
        return String.format("%s%s(%s)",
                target,
                genericArgs.length == 0 ? "" : '[' + implode(", ", genericArgs) + ']',
                implode(", ", args));
    }
}

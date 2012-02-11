package b.exp;

import static util.StringUtils.implode;

import java.util.Arrays;

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
        c.ty.Type[] refinedGenericArgs = Type.refineAll(genericArgs, typeCtx, methodCtx);
        c.exp.Expression[] refinedArgs = Expression.refineAll(args, typeCtx, methodCtx);

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

            // This is an instance method invocation, but it could have one of two forms:
            //     exp.method(...) - a normal instance method call
            //     exp.field(...) - invoking the get method of a field
            // We postpone resolving this ambiguity by returning the general InstanceMemberInvocation.
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
        StringBuilder sb = new StringBuilder();
        sb.append(target);
        if (genericArgs.length > 0)
            sb.append(Arrays.toString(genericArgs));
        sb.append('(').append(implode(", ", args)).append(')');
        return sb.toString();
    }
}

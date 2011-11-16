package ast.exp;

import ast.Type;
import common.FullTypeDesc;
import ctx.CodeContext;
import rst.exp.ClassMethodInvocation;
import rst.exp.InstanceMethodInvocation;
import rst.exp.InstanceMethodInvocationOrGet;

import static util.StringUtils.implode;

public class Invocation extends Expression {
    public final Expression target;
    public final Type[] genericArgs;
    public final Expression[] args;

    public Invocation(Expression target, Type[] genericArgs, Expression[] args) {
        this.target = target;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    public rst.exp.Expression refine(CodeContext ctx) {
        // We have to deal with these types of invocations:
        //     class method: SomeClass.myMethod(...)
        //     normal instance method: something.myMethod(...)
        //     functor: something.someCollection(idx)
        //     class get: SomeClass(...)

        FullTypeDesc[] genericArgsRef = ctx.resolveAllFull(genericArgs);
        rst.exp.Expression[] argsRef = new rst.exp.Expression[args.length];
        for (int i = 0; i < argsRef.length; ++i)
            argsRef[i] = args[i].refine(ctx);

        if (target instanceof MemberAccess) {
            MemberAccess memberAcc = (MemberAccess) target;
            Expression owner = memberAcc.target;
            if (owner instanceof VarExp) {
                String typeName = ((VarExp) owner).id;
                if (ctx.hasRawType(typeName))
                    return new ClassMethodInvocation(ctx.resolveRaw(typeName), memberAcc.memberName,
                                                     genericArgsRef, argsRef);
            }
            return new InstanceMethodInvocationOrGet(owner.refine(ctx),
                    memberAcc.memberName, genericArgsRef, argsRef);
        }

        if (target instanceof VarExp) {
            String typeName = ((VarExp) target).id;
            if (ctx.hasRawType(typeName))
                // class get, e.g. MyClass[Int](...)
                return new ClassMethodInvocation(ctx.resolveRaw(typeName), "get", genericArgsRef, argsRef);
        }

        // Must be a get call, such as getArray()(index)
        return new InstanceMethodInvocation(target.refine(ctx), "get", genericArgsRef, argsRef);
    }

    public String toString() {
        return String.format("%s(%s)", target, implode(", ", args));
    }
}

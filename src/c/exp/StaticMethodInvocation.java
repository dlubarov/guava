package c.exp;

import static util.StringUtils.implode;
import c.*;
import c.ty.Type;

import common.RawType;
import d.Opcodes;

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

    private MethodDef getMethod(CodeContext ctx) {
        // TODO: It would be possible to avoid inferring types for each arg, instead using hasType.
        Type[] argTypes = Expression.inferAllTypes(args, ctx);
        TypeDef ownerDef = ctx.project.resolve(owner);
        return ownerDef.getStaticMethod(methodName, genericArgs, argTypes, ctx);
    }

    @Override
    public Type inferType(CodeContext ctx) {
        MethodDef m = getMethod(ctx);
        // TODO: Will need to add possible type generics once static invocation of instance methods is supported.
        return m.returnType.withGenericArgs(null, genericArgs);
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        MethodDef m = getMethod(ctx);
        int methodIndex = ctx.method.getMethodTableIndex(m);
        int[] genericArgIndices = m.getFullTypeTableIndices(genericArgs);
        return new CodeTree(
                Expression.compileAll(args, ctx),
                Opcodes.INVOKE_STATIC, methodIndex, genericArgIndices
        );
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

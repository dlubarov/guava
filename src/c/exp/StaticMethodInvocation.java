package c.exp;

import static util.StringUtils.implode;

import java.util.Arrays;

import util.ArrayUtils;

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
        TypeDef ownerDef = Project.singleton.resolve(owner);
        return ownerDef.getStaticMethod(methodName, genericArgs, argTypes, ctx);
    }

    @Override
    public Type inferType(CodeContext ctx) {
        MethodDef m = getMethod(ctx);
        Type[] typeGenerics;
        if (m.isStatic)
            typeGenerics = null;
        else {
            Type thisType = args[0].inferType(ctx);
            typeGenerics = thisType.asSupertype(owner, ctx).genericArgs;
        }
        return m.returnType.withGenericArgs(typeGenerics, genericArgs);
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        MethodDef m = getMethod(ctx);
        int methodIndex = ctx.method.getMethodTableIndex(m);

        int[] genericArgIndices = ctx.method.getFullTypeTableIndices(genericArgs);
        Integer[] genericArgIndicesBoxed = ArrayUtils.boxArray(genericArgIndices);
        CodeTree genericArgIndicesTree = new CodeTree((Object[]) genericArgIndicesBoxed);

        return new CodeTree(
                Expression.compileAll(args, ctx),
                Opcodes.INVOKE_STATIC, methodIndex, genericArgIndicesTree
        );
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        return compile(Type.coreTop, ctx);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(owner).append('.').append(methodName);
        if (genericArgs.length > 0)
            sb.append(Arrays.toString(genericArgs));
        sb.append('(').append(implode(", ", args)).append(')');
        return sb.toString();
    }
}

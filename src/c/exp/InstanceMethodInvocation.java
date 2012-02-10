package c.exp;

import static util.StringUtils.implode;

import java.util.*;

import common.NiftyException;

import c.*;
import c.ty.*;
import d.Opcodes;

public class InstanceMethodInvocation extends Expression {
    public final Expression target;
    public final String methodName;
    public final Type[] genericArgs;
    public final Expression[] args;

    public InstanceMethodInvocation(Expression target, String methodName,
            Type[] genericArgs, Expression[] args) {
        this.target = target;
        this.methodName = methodName;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    private MethodDef getMethod(CodeContext ctx) {
        Type targetType = target.inferType(ctx);
        Type[] argTypes = Expression.inferAllTypes(args, ctx);
        Set<MethodDef> options = new HashSet<MethodDef>();
        for (ParameterizedType concreteSuper : targetType.getConcreteSupertypes(ctx.type, ctx.method))
            try {
                TypeDef superDef = ctx.project.resolve(concreteSuper.rawType);
                options.add(superDef.getInstanceMethod(methodName, genericArgs, argTypes, ctx));
            } catch (NoSuchElementException e) {}
        if (options.isEmpty())
            throw new NoSuchElementException(String.format("%s has no method named %s", target, methodName));
        if (options.size() > 1)
            throw new NiftyException("%s inherits multiple methods named %s", target, methodName);
        return options.iterator().next();
    }

    // Tests whether the method being invoked actually exists.
    public boolean isValid(CodeContext ctx) {
        try {
            getMethod(ctx);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public Type inferType(CodeContext ctx) {
        MethodDef method = getMethod(ctx);
        ParameterizedType targetAsMethodOwner = target.inferType(ctx).asSupertype(method.owner, ctx);
        return method.returnType.withGenericArgs(targetAsMethodOwner.genericArgs, genericArgs);
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        MethodDef m = getMethod(ctx);
        int methodIndex = ctx.method.getMethodTableIndex(m);
        int[] genericArgIndices = m.getFullTypeTableIndices(genericArgs);
        return new CodeTree(
                target.compile(ctx),
                Expression.compileAll(args, ctx),
                Opcodes.INVOKE_VIRTUAL, methodIndex, genericArgIndices
        );
    }

    @Override
    public String toString() {
        return String.format("%s.%s%s(%s)",
                target, methodName,
                genericArgs.length == 0 ? "" : '[' + implode(", ", genericArgs) + ']',
                implode(", ", args));
    }
}

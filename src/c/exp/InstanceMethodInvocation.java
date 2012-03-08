package c.exp;

import static util.StringUtils.implode;

import java.util.*;

import util.ArrayUtils;

import common.*;

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
                TypeDef superDef = Project.singleton.resolve(concreteSuper.rawType);
                options.add(superDef.getInstanceMethod(methodName, concreteSuper.genericArgs, genericArgs, argTypes, ctx));
            } catch (NoSuchElementException e) {}
        if (options.isEmpty())
            throw new NoSuchElementException(String.format(
                    "%s has no method named '%s' with argument types %s.",
                    target, methodName, Arrays.toString(argTypes)));
        if (options.size() > 1)
            throw new NiftyException(
                    "%s inherits multiple methods named '%s'.",
                    target, methodName);
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

    private static Integer getNativeOp(MethodDef m) {
        if (m.owner.equals(RawType.coreBool)) {
            if (m.paramTypes.length == 0) {
                if (m.name.equals("!"))
                    return Opcodes.BOOL_NEG;
            }
        }
        if (m.owner.equals(RawType.coreInt)) {
            if (m.paramTypes.length == 0) {
                if (m.name.equals("-"))
                    return Opcodes.INT_NEG;
            }
            if (m.paramTypes.length == 1) {
                Type paramTy = m.paramTypes[0];
                if (m.name.equals("+") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_ADD;
                if (m.name.equals("-") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_SUB;
                if (m.name.equals("*") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_MUL;
                if (m.name.equals("/") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_DIV;
                if (m.name.equals("%") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_MOD;
                if (m.name.equals("|") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_IOR;
                if (m.name.equals("^") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_XOR;
                if (m.name.equals("&") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_AND;
                if (m.name.equals("<<") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_LSHIFT;
                if (m.name.equals(">>>") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_RSHIFT_UNSIGNED;
                if (m.name.equals(">>") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_RSHIFT_SIGNED;
                if (m.name.equals("compareTo") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_COMPARETO;
                if (m.name.equals("<") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_LT;
                if (m.name.equals("<=") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_LTE;
                if (m.name.equals(">") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_GT;
                if (m.name.equals(">=") && paramTy.equals(Type.coreInt))
                    return Opcodes.INT_GTE;
            }
        }
        return null;
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        MethodDef m = getMethod(ctx);
        TypeDef owner = Project.singleton.resolve(m.owner);
        CodeTree targetCode = target.compile(ctx);
        CodeTree argumentCode = Expression.compileAll(args, ctx);

        // Try to use a native op, if possible.
        Integer nativeOp = getNativeOp(m);
        if (nativeOp != null)
            return new CodeTree(targetCode, argumentCode, nativeOp);

        // Do a normal virtual method invokation.
        int methodIndex = ctx.method.getMethodTableIndex(m);
        int[] genericArgIndices = ctx.method.getFullTypeTableIndices(genericArgs);
        Integer[] genericArgIndicesBoxed = ArrayUtils.boxArray(genericArgIndices);
        CodeTree genericArgIndicesTree = new CodeTree((Object[]) genericArgIndicesBoxed);

        // Can we make a cheap static call, or do we need to go through the vtable?
        boolean canUseStatic = m.isSealed || owner.isSealed;
        int invokeOp = canUseStatic ? Opcodes.INVOKE_STATIC : Opcodes.INVOKE_VIRTUAL;

        return new CodeTree(
                targetCode, argumentCode,
                invokeOp, methodIndex, genericArgIndicesTree
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(target).append('.').append(methodName);
        if (genericArgs.length > 0)
            sb.append(Arrays.toString(genericArgs));
        sb.append('(').append(implode(", ", args)).append(')');
        return sb.toString();
    }
}

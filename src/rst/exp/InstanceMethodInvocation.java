package rst.exp;

import common.*;
import comp.CodeTree;
import rctx.CodeRCtx;
import rst.*;
import vm.Opcodes;

import java.util.Arrays;

import static util.StringUtils.implode;

public class InstanceMethodInvocation extends Expression {
    private final Expression target;
    private final String methodName;
    private final FullTypeDesc[] genericArgs;
    private final Expression[] args;

    public InstanceMethodInvocation(Expression target, String methodName,
                                    FullTypeDesc[] genericArgs, Expression[] args) {
        this.target = target;
        this.methodName = methodName;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    private MethodDef getMethod(CodeRCtx ctx, FullTypeDesc[] argTypes, FullTypeDesc[] parentTypes) {
        // FIXME: need to search up the type hierarchy for appropriate method.
        // This temporary fix only searches Object, ignoring other generic upper bounds.
        return ctx.resolve(new RawTypeDesc("core", "Object"))
                .getMatchingInstanceMethod(ctx, methodName, FullTypeDesc.NONE, genericArgs, argTypes);
    }

    private MethodDef getMethod(CodeRCtx ctx) {
        FullTypeDesc[] argTypes = new FullTypeDesc[args.length];
        for (int i = 0; i < argTypes.length; ++i)
            argTypes[i] = args[i].inferType(ctx);

        FullTypeDesc targetTypeDesc = target.inferType(ctx);
        if (targetTypeDesc instanceof NormalFullTypeDesc) {
            NormalFullTypeDesc normTypeDesc = (NormalFullTypeDesc) targetTypeDesc;
            TypeDef targetType = ctx.resolve(normTypeDesc.raw);
            return targetType.getMatchingInstanceMethod(ctx,
                    methodName, normTypeDesc.genericArgs, genericArgs, argTypes);
        } else if (targetTypeDesc instanceof TypeGenericFullTypeDesc) {
            TypeGenericFullTypeDesc genericTypeDesc = (TypeGenericFullTypeDesc) targetTypeDesc;
            RawTypeDesc ownerTypeDesc = genericTypeDesc.owner;
            TypeDef ownerType = ctx.resolve(ownerTypeDesc);
            GenericInfo genericInfo = ownerType.genericInfos[genericTypeDesc.index];
            return getMethod(ctx, argTypes, genericInfo.parentTypes);
        } else if (targetTypeDesc instanceof MethodGenericFullTypeDesc) {
            throw new RuntimeException("FIXME: impl");
        } else
            throw new RuntimeException("shouldn't get here");
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        FullTypeDesc targetType = target.inferType(ctx);
        MethodDef meth = getMethod(ctx);
        FullTypeDesc result = meth.retType;

        FullTypeDesc[] typeGenerics = FullTypeDesc.NONE;
        if (targetType instanceof NormalFullTypeDesc) {
            NormalFullTypeDesc ntd = (NormalFullTypeDesc) targetType;
            typeGenerics = ntd.genericsFor(
                    meth.desc.owner,
                    ctx.methodCtx.globalCtx);
        }
        result = result.withGenerics(typeGenerics, genericArgs);

        return result;
    }

    public CodeTree compile(CodeRCtx ctx) {
        MethodDef method = getMethod(ctx);
        assert !method.isStatic;
        CodeTree[] argCode = new CodeTree[args.length];
        for (int i = 0; i < argCode.length; ++i)
            argCode[i] = args[i].compile(ctx);
        CodeTree allArgCode = new CodeTree((Object[]) argCode);

        Integer[] genericArgIndices = new Integer[genericArgs.length];
        for (int i = 0; i < genericArgIndices.length; ++i)
            genericArgIndices[i] = ctx.getFullTypeIndex(genericArgs[i]);

        return new CodeTree(target.compile(ctx), allArgCode,
                Opcodes.INVOKE_VIRTUAL, ctx.getMethodIndex(method.desc),
                genericArgIndices.length, new CodeTree((Object[]) genericArgIndices));
    }

    public String toString() {
        return String.format("%s.%s%s(%s)",
                target, methodName,
                genericArgs.length == 0 ? "" : Arrays.toString(genericArgs),
                implode(", ", args));
    }
}

package rst.exp;

import common.*;
import comp.CodeTree;
import rctx.CodeRCtx;
import rst.MethodDef;
import rst.TypeDef;
import vm.Opcodes;

import java.util.*;

import static util.StringUtils.implode;

public class ClassMethodInvocation extends Expression {
    private final RawTypeDesc owner;
    private final String methodName;
    private final FullTypeDesc[] genericArgs;
    private final Expression[] args;

    public ClassMethodInvocation(RawTypeDesc owner, String methodName,
            FullTypeDesc[] genericArgs, Expression[] args) {
        this.owner = owner;
        this.methodName = methodName;
        this.genericArgs = genericArgs;
        this.args = args;
    }

    // Retrieves the implicit type-level generic args for a static invocation
    // of an instance method.
    private FullTypeDesc[] typeGenericArgs(CodeRCtx ctx) {
        TypeDef ownerType = ctx.resolve(owner);
        int n = ownerType.genericInfos.length;
        if (n == 0)
            return FullTypeDesc.NONE;
        FullTypeDesc thisType = args[0].inferType(ctx);
        if (!(thisType instanceof NormalFullTypeDesc))
            throw new RuntimeException(String.format(
                    "%s has type %s, which has no generic arguments",
                    args[0], thisType));
        NormalFullTypeDesc thisNormType = (NormalFullTypeDesc) thisType;
        TypeDef thisTypeDef = ctx.resolve(thisNormType.raw);
        FullTypeDesc[] result = new FullTypeDesc[n];
        for (int i = 0; i < result.length; ++i)
            result[i] = thisTypeDef.inMyGenerics(
                    new TypeGenericFullTypeDesc(owner, i),
                    thisNormType.genericArgs,
                    ctx.methodCtx.globalCtx);
        return result;
    }

    private MethodDef getMethod(CodeRCtx ctx) {
        TypeDef ownerType = ctx.resolve(owner);
        FullTypeDesc[] argTypes = new FullTypeDesc[args.length];
        for (int i = 0; i < argTypes.length; ++i)
            argTypes[i] = args[i].inferType(ctx);
        List<MethodDef> options = new ArrayList<MethodDef>();

        methodsearch:
        for (MethodDef meth : ownerType.methods) {
            if (!meth.name.equals(methodName))
                continue;
            if (meth.numGenericParams != genericArgs.length)
                continue;

            // Fetch the method's parameter types
            FullTypeDesc[] paramTypes;
            if (meth.isStatic)
                paramTypes = meth.paramTypes.clone();
            else {
                // Static invocation of instance method must have at least one argument ("this")
                if (args.length == 0)
                    continue;
                FullTypeDesc[] typeGenericArgs = typeGenericArgs(ctx);
                paramTypes = new FullTypeDesc[meth.paramTypes.length + 1];
                paramTypes[0] = new NormalFullTypeDesc(owner, typeGenericArgs); // "this"
                System.arraycopy(meth.paramTypes, 0, paramTypes, 1, meth.paramTypes.length);
                for (int i = 1; i < paramTypes.length; ++i)
                    paramTypes[i] = paramTypes[i].withTypeGenerics(typeGenericArgs);
            }

            // Check for correct number of arguments
            if (paramTypes.length != args.length)
                continue;

            // Apply the generic args of the method invocation
            for (int i = 0; i < paramTypes.length; ++i)
                paramTypes[i] = paramTypes[i].withMethodGenerics(genericArgs);

            // Check that argument types match
            for (int i = 0; i < paramTypes.length; ++i)
                try {
                if (!argTypes[i].isSubtype(paramTypes[i], ctx.methodCtx.globalCtx))
                    continue methodsearch;
                } catch (RuntimeException e) {
                    throw new RuntimeException(String.format(
                            "error matching %s against %s",
                            argTypes[i], paramTypes[i]), e);
                }

            options.add(meth);
        }

        if (options.isEmpty())
            throw new NoSuchElementException(String.format("no matching method for %s.%s(%s)",
                    owner, methodName, implode(", ", argTypes)));
        if (options.size() > 1)
            throw new NoSuchElementException(String.format("multiple matching methods for %s.%s(%s)",
                    owner, methodName, implode(", ", argTypes)));
        return options.get(0);
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        MethodDef meth = getMethod(ctx);
        FullTypeDesc retType = meth.retType;
        if (meth.isStatic)
            retType = retType.withMethodGenerics(genericArgs);
        else
            retType = retType.withGenerics(typeGenericArgs(ctx), genericArgs);
        return retType;
    }

    public CodeTree compile(CodeRCtx ctx) {
        MethodDef method = getMethod(ctx);
        CodeTree[] argCode = new CodeTree[args.length];
        for (int i = 0; i < argCode.length; ++i)
            argCode[i] = args[i].compile(ctx);
        CodeTree allArgCode = new CodeTree((Object[]) argCode);

        Integer[] genericArgIndices = new Integer[genericArgs.length];
        for (int i = 0; i < genericArgIndices.length; ++i)
            genericArgIndices[i] = ctx.getFullTypeIndex(genericArgs[i]);

        return new CodeTree(allArgCode, Opcodes.INVOKE_STATIC, ctx.getMethodIndex(method.desc),
                genericArgIndices.length, new CodeTree((Object[]) genericArgIndices));
    }

    public String toString() {
        return String.format("%s.%s%s(%s)",
                owner, methodName,
                genericArgs.length == 0 ? "" : Arrays.toString(genericArgs),
                implode(", ", args));
    }
}

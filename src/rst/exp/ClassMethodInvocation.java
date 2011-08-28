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

    public ClassMethodInvocation(RawTypeDesc owner, String methodName, FullTypeDesc[] genericArgs, Expression[] args) {
        this.owner = owner;
        this.methodName = methodName;
        this.genericArgs = genericArgs;
        this.args = args;
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
                paramTypes = meth.paramTypes;
            else {
                paramTypes = new FullTypeDesc[meth.paramTypes.length + 1];
                paramTypes[0] = new NormalFullTypeDesc(owner); // "this"
                System.arraycopy(meth.paramTypes, 0, paramTypes, 1, meth.paramTypes.length);
            }

            // Check for correct number of arguments
            if (paramTypes.length != args.length)
                continue;

            // Apply the generic args of the method invocation
            for (int i = 0; i < paramTypes.length; ++i)
                paramTypes[i] = paramTypes[i].withMethodGenerics(genericArgs);

            // Check that argument types match
            for (int i = 0; i < paramTypes.length; ++i)
                if (!argTypes[i].isSubtype(paramTypes[i], ctx))
                    continue methodsearch;
            
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
        return getMethod(ctx).retType.withMethodGenerics(genericArgs);
    }

    public CodeTree compile(CodeRCtx ctx) {
        MethodDef method = getMethod(ctx);
        CodeTree[] argCode = new CodeTree[args.length];
        for (int i = 0; i < argCode.length; ++i)
            argCode[i] = args[i].compile(ctx);
        CodeTree allArgCode = new CodeTree((Object[]) argCode);
        return new CodeTree(allArgCode, Opcodes.INVOKE_STATIC, ctx.getMethodIndex(method.desc));
    }
    
    public String toString() {
        return String.format("%s.%s%s(%s)",
                owner, methodName,
                genericArgs.length == 0 ? "" : Arrays.toString(genericArgs),
                implode(", ", args));
    }
}

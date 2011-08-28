package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import rst.MethodDef;
import rst.TypeDef;
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

    private MethodDef getMethod(CodeRCtx ctx) {
        FullTypeDesc targetTypeDesc = target.inferType(ctx);
        // TODO: this assumes generic types have no methods, which will be no longer valid when bounds are added
        NormalFullTypeDesc normTypeDesc = (NormalFullTypeDesc) targetTypeDesc;
        TypeDef targetType = ctx.resolve(normTypeDesc.raw);
        
        FullTypeDesc[] argTypes = new FullTypeDesc[args.length];
        for (int i = 0; i < argTypes.length; ++i)
            argTypes[i] = args[i].inferType(ctx);
        return targetType.getMatchingInstanceMethod(ctx, methodName, genericArgs, argTypes);
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        FullTypeDesc targetType = target.inferType(ctx);
        FullTypeDesc result = getMethod(ctx).retType;
        if (targetType instanceof NormalFullTypeDesc)
            result = result.withTypeGenerics(((NormalFullTypeDesc) target.inferType(ctx)).genericArgs);
        result = result.withMethodGenerics(genericArgs);
        return result;
    }

    public CodeTree compile(CodeRCtx ctx) {
        MethodDef method = getMethod(ctx);
        assert !method.isStatic;
        CodeTree[] argCode = new CodeTree[args.length];
        for (int i = 0; i < argCode.length; ++i)
            argCode[i] = args[i].compile(ctx);
        CodeTree allArgCode = new CodeTree((Object[]) argCode);
        return new CodeTree(target.compile(ctx), allArgCode,
                Opcodes.INVOKE_VIRTUAL, ctx.getMethodIndex(method.desc));
    }

    public String toString() {
        return String.format("%s.%s%s(%s)",
                target, methodName,
                genericArgs.length == 0 ? "" : Arrays.toString(genericArgs),
                implode(", ", args));
    }
}

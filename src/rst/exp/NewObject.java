package rst.exp;

import java.util.*;

import common.*;
import comp.CodeTree;
import rctx.CodeRCtx;
import rst.*;
import vm.Opcodes;

import static util.StringUtils.implode;

public class NewObject extends Expression {
    private final NormalFullTypeDesc type;
    private final Expression[] args;

    public NewObject(NormalFullTypeDesc type, Expression[] args) {
        this.type = type;
        this.args = args;
    }

    private MethodDef getMethod(CodeRCtx ctx) {
        TypeDef ownerType = ctx.resolve(type.raw);
        FullTypeDesc[] argTypes = new FullTypeDesc[args.length];
        for (int i = 0; i < argTypes.length; ++i)
            argTypes[i] = args[i].inferType(ctx);
        List<MethodDef> options = new ArrayList<MethodDef>();

        methodsearch:
        for (MethodDef meth : ownerType.methods) {
            if (!meth.name.equals("init"))
                continue;
            if (meth.isStatic)
                continue;
            if (meth.numGenericParams != 0)
                continue;
            if (meth.paramTypes.length != args.length)
                continue;
            for (int i = 0; i < argTypes.length; ++i)
                if (!argTypes[i].isSubtype(
                        meth.paramTypes[i].withTypeGenerics(type.genericArgs),
                        ctx.methodCtx.globalCtx))
                    continue methodsearch;
            options.add(meth);
        }

        if (options.isEmpty())
            throw new NoSuchElementException(String.format("no matching constructor for %s.init(%s)",
                    type.raw, implode(", ", argTypes)));
        if (options.size() > 1)
            throw new NoSuchElementException(String.format("multiple matching constructors for %s.init(%s)",
                    type.raw, implode(", ", argTypes)));
        return options.get(0);
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return type;
    }

    public CodeTree compile(CodeRCtx ctx) {
        if (ctx.resolve(type.raw).isAbstract)
            throw new RuntimeException("instantiation of abstract type " + type.raw);
        MethodDef method = getMethod(ctx);
        CodeTree[] argCode = new CodeTree[args.length];
        for (int i = 0; i < argCode.length; ++i)
            argCode[i] = args[i].compile(ctx);
        CodeTree allArgCode = new CodeTree((Object[]) argCode);
        return new CodeTree(
                Opcodes.NEW, ctx.getFullTypeIndex(type),
                Opcodes.DUP, allArgCode,
                Opcodes.INVOKE_STATIC, ctx.getMethodIndex(method.desc), 0,
                Opcodes.POP // discard void result of init method
        );
    }

    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}

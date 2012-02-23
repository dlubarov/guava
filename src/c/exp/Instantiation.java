package c.exp;

import static util.StringUtils.implode;
import c.*;
import c.ty.*;
import d.Opcodes;

public class Instantiation extends Expression {
    public final ParameterizedType type;
    public final Expression[] args;

    public Instantiation(ParameterizedType type, Expression[] args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return type;
    }

    private MethodDef getConstructor(CodeContext ctx) {
        TypeDef typeDef = Project.singleton.resolve(type.rawType);
        Type[] argTypes = Expression.inferAllTypes(args, ctx);
        MethodDef m = typeDef.getInstanceMethod("init", type.genericArgs, Type.NONE, argTypes, ctx);
        assert m.owner.equals(type.rawType);
        assert m.name.equals("init");
        assert m.returnType.equals(Type.coreUnit);
        return m;
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        MethodDef constructor = getConstructor(ctx);
        int typeIdx = ctx.method.getFullTypeTableIndex(type);
        int constructorIdx = ctx.method.getMethodTableIndex(constructor);
        return new CodeTree(
                Opcodes.NEW, typeIdx,
                Opcodes.DUP, Expression.compileAll(args, ctx),
                Opcodes.INVOKE_STATIC, constructorIdx,
                Opcodes.POP
        );
    }

    @Override
    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}

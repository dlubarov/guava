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
        return typeDef.getInstanceMethod("init", Type.NONE, argTypes, ctx);
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        MethodDef constructor = getConstructor(ctx);
        return new CodeTree(
                Opcodes.NEW, ctx.method.getFullTypeTableIndex(type),
                Opcodes.DUP, Expression.compileAll(args, ctx),
                Opcodes.INVOKE_STATIC, ctx.method.getMethodTableIndex(constructor));
    }

    @Override
    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}

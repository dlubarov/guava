package c.exp;

import static util.StringUtils.implode;

import common.NiftyException;

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
        if (typeDef.isAbstract)
            throw new NiftyException("Can't instantiate abstract type '%s'.", type);
        Type[] argTypes = Expression.inferAllTypes(args, ctx);
        MethodDef m = typeDef.getInstanceMethod("init", type.genericArgs, Type.NONE, argTypes, ctx);
        assert m.owner.equals(type.rawType);
        assert m.name.equals("init");
        assert m.returnType.equals(Type.coreUnit);
        return m;
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        MethodDef constructor = getConstructor(ctx);
        int constructorIdx = ctx.method.getMethodTableIndex(constructor);

        // If type has no generic arguments, use the more efficient NEW_NO_GENERICS.
        int typeIdx, newOp;
        if (type.genericArgs.length == 0) {
            typeIdx = ctx.method.getRawTypeTableIndex(type.rawType);
            newOp = Opcodes.NEW_NO_GENERICS;
        } else {
            typeIdx = ctx.method.getFullTypeTableIndex(type);
            newOp = Opcodes.NEW;
        }

        return new CodeTree(
                // Stack: []
                newOp, typeIdx,
                // Stack: [obj]
                Opcodes.DUP,
                // Stack: [obj, obj]
                Expression.compileAll(args, ctx),
                // Stack: [obj, obj, arg...]
                Opcodes.INVOKE_STATIC, constructorIdx,
                // Stack: [obj, unit]
                Opcodes.POP
                // Stack: [obj]
        );
    }

    @Override
    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}

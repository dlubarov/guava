package c.exp;

import common.*;

import c.*;
import c.ty.*;

public abstract class Expression {
    public abstract Type inferType(CodeContext ctx);

    public boolean hasType(Type type, CodeContext ctx) {
        if (type.equals(new ParameterizedType(RawType.coreTop)))
            return true;
        return inferType(ctx).isSubtype(type, ctx);
    }

    public abstract CodeTree compile(CodeContext ctx);

    public CodeTree compileWithTypeHint(Type requiredType, CodeContext ctx) {
        if (!hasType(requiredType, ctx))
            throw new NiftyException("'%s' does not conform to expected type %s.", this, requiredType);
        return compile(ctx);
    }

    public static Type[] inferAllTypes(Expression[] expressions, CodeContext ctx) {
        Type[] types = new Type[expressions.length];
        for (int i = 0; i < types.length; ++i)
            types[i] = expressions[i].inferType(ctx);
        return types;
    }

    public static CodeTree compileAll(Expression[] expressions, CodeContext ctx) {
        CodeTree[] parts = new CodeTree[expressions.length];
        for (int i = 0; i < parts.length; ++i)
            parts[i] = expressions[i].compile(ctx);
        return new CodeTree((Object[]) parts);
    }

    @Override
    public abstract String toString();
}

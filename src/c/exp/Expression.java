package c.exp;

import common.*;

import c.*;
import c.ty.*;

public abstract class Expression {
    public abstract Type inferType(CodeContext ctx);

    // Check whether an expression CAN conform to a certain type.
    public boolean hasType(Type type, CodeContext ctx) {
        if (type.equals(new ParameterizedType(RawType.coreTop)))
            return true;
        return inferType(ctx).isSubtype(type, ctx);
    }

    // Compile an expression which must conform to some required type.
    // Note, callers CANNOT count on implementations of this method to check that the type
    // requirement is satisfied. They should check this with hasType.
    // This method is just here to support polymorphic expressions, like {}.
    public abstract CodeTree compile(Type requiredType, CodeContext ctx);

    // Compile an expression whose type we don't care about.
    // This should  be used for compiling eval statements, for example, because the value
    // returned by the evaluated expression is simply discarded.
    public CodeTree compile(CodeContext ctx) {
        return compile(Type.coreTop, ctx);
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

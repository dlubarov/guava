package b.exp;

import b.*;

public abstract class Expression {
    public abstract c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx);

    public static c.exp.Expression[] refineAll(Expression[] expressions, TypeDef typeCtx, MethodDef methodCtx) {
        c.exp.Expression[] result = new c.exp.Expression[expressions.length];
        for (int i = 0; i < result.length; ++i)
            result[i] = expressions[i].refine(typeCtx, methodCtx);
        return result;
    }

    @Override
    public abstract String toString();
}

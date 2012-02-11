package b.exp;

import static util.StringUtils.implode;
import b.*;

public class Instantiation extends Expression {
    public final Type type;
    public final Expression[] args;

    public Instantiation(Type type, Expression[] args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        c.exp.Expression[] refinedArgs = Expression.refineAll(args, typeCtx, methodCtx);
        return new c.exp.Instantiation(
                (c.ty.ParameterizedType) type.refine(typeCtx, methodCtx),
                refinedArgs);
    }

    @Override
    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}

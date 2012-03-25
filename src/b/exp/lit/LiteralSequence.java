package b.exp.lit;

import static util.StringUtils.implode;
import b.*;
import b.exp.Expression;

public class LiteralSequence extends Expression {
    public final Expression[] elements;

    public LiteralSequence(Expression[] elements) {
        this.elements = elements;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        c.exp.Expression[] refinedElements = Expression.refineAll(elements, typeCtx, methodCtx);
        return new c.exp.lit.LiteralSequence(refinedElements);
    }

    @Override
    public String toString() {
        return '{' + implode(", ", elements) + '}';
    }
}

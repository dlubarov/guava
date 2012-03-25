package a.exp.lit;

import static util.StringUtils.implode;
import a.exp.Expression;

public class LiteralSequence extends Expression {
    public final Expression[] elements;

    public LiteralSequence(Expression[] elements) {
        this.elements = elements;
    }

    @Override
    public b.exp.Expression refine() {
        b.exp.Expression[] refinedElements = Expression.refineAll(elements);
        return new b.exp.lit.LiteralSequence(refinedElements);
    }

    @Override
    public String toString() {
        return '{' + implode(", ", elements) + '}';
    }
}

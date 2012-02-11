package a.exp;

import static util.StringUtils.implode;

public class LiteralSequence extends Expression {
    public final Expression[] elements;

    public LiteralSequence(Expression[] elements) {
        this.elements = elements;
    }

    @Override
    public b.exp.Expression refine() {
        b.exp.Expression[] refinedElements = Expression.refineAll(elements);
        return new b.exp.LiteralSequence(refinedElements);
    }

    @Override
    public String toString() {
        return '{' + implode(", ", elements) + '}';
    }
}

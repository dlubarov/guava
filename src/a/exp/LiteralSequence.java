package a.exp;

import util.StringUtils;

public class LiteralSequence extends Expression {
    public final Expression[] elements;

    public LiteralSequence(Expression[] elements) {
        this.elements = elements;
    }

    @Override
    public b.exp.Expression refine() {
        b.exp.Expression[] refinedElements = new b.exp.Expression[elements.length];
        for (int i = 0; i < refinedElements.length; ++i)
            refinedElements[i] = elements[i].refine();
        return new b.exp.LiteralSequence(refinedElements);
    }

    @Override
    public String toString() {
        return "{" + StringUtils.implode(", ", elements) + "}";
    }
}

package b.exp;

import util.StringUtils;
import b.*;

public class LiteralSequence extends Expression {
    public final Expression[] elements;

    public LiteralSequence(Expression[] elements) {
        this.elements = elements;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        c.exp.Expression[] refinedElements = new c.exp.Expression[elements.length];
        for (int i = 0; i < refinedElements.length; ++i)
            refinedElements[i] = elements[i].refine(typeCtx, methodCtx);
        return new c.exp.LiteralSequence(refinedElements);
    }

    @Override
    public String toString() {
        return "{" + StringUtils.implode(", ", elements) + "}";
    }
}

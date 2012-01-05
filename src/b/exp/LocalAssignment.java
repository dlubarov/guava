package b.exp;

import b.*;

public class LocalAssignment extends Expression {
    public final String localName;
    public final Expression value;

    public LocalAssignment(String localName, Expression value) {
        this.localName = localName;
        this.value = value;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.LocalAssignment(localName, value.refine(typeCtx, methodCtx));
    }

    @Override
    public String toString() {
        return String.format("%s = %s", localName, value);
    }
}

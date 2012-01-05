package c.exp;

import c.CodeContext;
import c.ty.Type;

public class LocalAssignment extends Expression {
    public final String localName;
    public final Expression value;

    public LocalAssignment(String localName, Expression value) {
        this.localName = localName;
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new LocalGet(localName).inferType(ctx);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", localName, value);
    }
}

package c.exp;

import c.*;
import c.ty.*;

public abstract class Expression {
    public abstract Type inferType(CodeContext ctx);

    public boolean hasType(Type type, CodeContext ctx) {
        if (type.equals(new ParameterizedType("core", "Top")))
            return true;
        return inferType(ctx).isSubtype(type, ctx);
    }

    @Override
    public abstract String toString();
}
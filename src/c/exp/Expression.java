package c.exp;

import common.RawType;

import c.*;
import c.ty.*;

public abstract class Expression {
    public abstract Type inferType(CodeContext ctx);

    public boolean hasType(Type type, CodeContext ctx) {
        if (type.equals(new ParameterizedType(RawType.coreTop)))
            return true;
        return inferType(ctx).isSubtype(type, ctx);
    }

    @Override
    public abstract String toString();
}

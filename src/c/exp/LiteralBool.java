package c.exp;

import common.RawType;

import c.CodeContext;
import c.ty.*;

public class LiteralBool extends Expression {
    public final boolean value;

    public LiteralBool(boolean value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType(RawType.coreBool);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}

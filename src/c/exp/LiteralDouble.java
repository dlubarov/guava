package c.exp;

import common.RawType;

import c.CodeContext;
import c.ty.*;

public class LiteralDouble extends Expression {
    public final double value;

    public LiteralDouble(double value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType(RawType.coreDouble);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}

package c.exp.lit;

import common.RawType;

import c.*;
import c.exp.Expression;
import c.ty.*;
import d.Opcodes;

public class LiteralLong extends Expression {
    public final long value;

    public LiteralLong(long value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType(RawType.coreLong);
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        int i1 = (int) (value >>> 32), i0 = (int) value;
        return new CodeTree(Opcodes.CONST_LONG, i1, i0);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}

package c.exp.lit;

import common.RawType;

import c.*;
import c.exp.Expression;
import c.ty.*;
import d.Opcodes;

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
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        long l = Double.doubleToLongBits(value);
        int i1 = (int) (l >>> 32), i0 = (int) l;
        return new CodeTree(Opcodes.CONST_DOUBLE, i1, i0);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}

package c.exp.lit;

import common.RawType;

import c.*;
import c.exp.Expression;
import c.ty.*;
import d.Opcodes;

public class LiteralInt extends Expression {
    public final int value;

    public LiteralInt(int value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType(RawType.coreInt);
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        return new CodeTree(Opcodes.CONST_INT, value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

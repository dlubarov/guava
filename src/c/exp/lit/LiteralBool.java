package c.exp.lit;

import common.RawType;

import c.*;
import c.exp.Expression;
import c.ty.*;
import d.Opcodes;

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
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        if (value)
            return new CodeTree(Opcodes.CONST_TRUE);
        return new CodeTree(Opcodes.CONST_FALSE);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}

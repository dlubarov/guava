package c.exp.lit;

import common.RawType;

import c.*;
import c.exp.Expression;
import c.ty.*;
import d.Opcodes;

public class LiteralChar extends Expression {
    public final char value;

    public LiteralChar(char value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType(RawType.coreChar);
    }

    @Override
    public CodeTree compile(Type requiredType, CodeContext ctx) {
        return new CodeTree(Opcodes.CONST_CHAR, (int) value);
    }

    @Override
    public String toString() {
        return Character.toString(value);
    }
}

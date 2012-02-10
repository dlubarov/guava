package c.exp;

import common.RawType;

import c.*;
import c.ty.*;
import d.Opcodes;

public class LiteralString extends Expression {
    public final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    @Override
    public Type inferType(CodeContext ctx) {
        return new ParameterizedType(RawType.coreString);
    }

    @Override
    public CodeTree compile(CodeContext ctx) {
        return new CodeTree(Opcodes.CONST_STRING, ctx.method.getStringTableIndex(value));
    }

    @Override
    public String toString() {
        return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"") + '"';
    }
}

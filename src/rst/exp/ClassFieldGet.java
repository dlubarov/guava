package rst.exp;

import common.FullTypeDesc;
import common.RawTypeDesc;
import rctx.CodeRCtx;
import rst.FieldDef;
import rst.TypeDef;

public class ClassFieldGet extends Expression {
    private final RawTypeDesc owner;
    private final String fieldName;

    public ClassFieldGet(RawTypeDesc owner, String fieldName) {
        this.owner = owner;
        this.fieldName = fieldName;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        TypeDef ownerType = ctx.resolve(owner);
        FieldDef field = ownerType.getStaticField(fieldName);
        return field.type;
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return String.format("%s.%s", owner, fieldName);
    }
}

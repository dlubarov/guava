package rst.exp;

import common.FullTypeDesc;
import common.RawTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import rst.FieldDef;
import rst.TypeDef;
import vm.Opcodes;

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

    public CodeTree compile(CodeRCtx ctx) {
        TypeDef ownerType = ctx.resolve(owner);
        int fieldIdx = ownerType.getStaticFieldIndex(fieldName);
        return new CodeTree(Opcodes.GET_STATIC_FIELD, ctx.getTypeIndex(owner), fieldIdx);
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return String.format("%s.%s", owner, fieldName);
    }
}

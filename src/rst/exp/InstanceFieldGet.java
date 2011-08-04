package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import rctx.CodeRCtx;
import rst.FieldDef;
import rst.TypeDef;

public class InstanceFieldGet extends Expression {
    private final Expression target;
    private final String fieldName;

    public InstanceFieldGet(Expression target, String fieldName) {
        this.target = target;
        this.fieldName = fieldName;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        // FIXME: doesn't work with fields owned by supertypes
        FullTypeDesc targetTypeDesc = target.inferType(ctx);
        // TODO: this assumes generic types have no fields, which will be no longer valid when bounds are added
        NormalFullTypeDesc normTypeDesc = (NormalFullTypeDesc) targetTypeDesc;
        TypeDef targetType = ctx.resolve(normTypeDesc.raw);
        FieldDef field = targetType.getField(fieldName);
        FullTypeDesc fieldType = field.type;
        return fieldType.withTypeGenerics(normTypeDesc.genericArgs);
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return String.format("%s.%s", target, fieldName);
    }
}

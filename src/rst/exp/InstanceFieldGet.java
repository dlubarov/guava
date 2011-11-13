package rst.exp;

import common.*;
import comp.CodeTree;
import rctx.CodeRCtx;
import rst.*;
import vm.Opcodes;

public class InstanceFieldGet extends Expression {
    private final Expression target;
    private final String fieldName;

    public InstanceFieldGet(Expression target, String fieldName) {
        this.target = target;
        this.fieldName = fieldName;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        // TODO: doesn't work with fields owned by supertypes
        FullTypeDesc targetTypeDesc = target.inferType(ctx);
        // TODO: this assumes generic types have no fields, which will be no longer valid when bounds are added
        NormalFullTypeDesc normTypeDesc = (NormalFullTypeDesc) targetTypeDesc;
        TypeDef targetType = ctx.resolve(normTypeDesc.raw);

        FieldDef field = targetType.getField(fieldName);
        return field.type.withTypeGenerics(normTypeDesc.genericArgs);
    }

    public CodeTree compile(CodeRCtx ctx) {
        // TODO: doesn't work with fields owned by supertypes
        FullTypeDesc targetTypeDesc = target.inferType(ctx);
        // TODO: this assumes generic types have no fields, which will be no longer valid when bounds are added
        NormalFullTypeDesc normTypeDesc = (NormalFullTypeDesc) targetTypeDesc;
        TypeDef targetType = ctx.resolve(normTypeDesc.raw);

        return new CodeTree(target.compile(ctx), Opcodes.GET_FIELD,
                targetType.getFieldIndex(fieldName));
    }

    @Override
    public boolean evalMayHaveSideEffects() {
        return false;
    }

    public String toString() {
        return String.format("%s.%s", target, fieldName);
    }
}

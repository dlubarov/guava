package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import rst.TypeDef;
import vm.Opcodes;

public class InstanceFieldSet extends Expression {
    private final Expression target;
    private final String fieldName;
    private final Expression value;

    public InstanceFieldSet(Expression target, String fieldName, Expression value) {
        this.target = target;
        this.fieldName = fieldName;
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return new InstanceFieldGet(target, fieldName).inferType(ctx);
    }

    public CodeTree compileHelper(CodeRCtx ctx, boolean giveResult) {
        assert ctx != null : "got a null context";
        // TODO: doesn't work with fields owned by supertypes
        FullTypeDesc targetTypeDesc = target.inferType(ctx);
        // TODO: this assumes generic types have no fields, which will be no longer valid when bounds are added
        NormalFullTypeDesc normTypeDesc = (NormalFullTypeDesc) targetTypeDesc;
        TypeDef targetType = ctx.resolve(normTypeDesc.raw);

        int idx = targetType.getFieldIndex(fieldName);

        if (giveResult)
            return new CodeTree(
                target.compile(ctx), Opcodes.DUP,
                value.compile(ctx),
                Opcodes.PUT_FIELD, idx,
                Opcodes.GET_FIELD, idx);
        else
            return new CodeTree(
                target.compile(ctx), value.compile(ctx),
                Opcodes.PUT_FIELD, idx);
    }

    public CodeTree compile(CodeRCtx ctx) {
        return compileHelper(ctx, true);
    }

    public CodeTree compileNoResult(CodeRCtx ctx) {
        return compileHelper(ctx, false);
    }

    public String toString() {
        return String.format("%s.%s = %s", target, fieldName, value);
    }
}

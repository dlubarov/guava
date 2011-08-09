package rst.exp;

import ast.TypeDef;
import common.FullTypeDesc;
import common.RawTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

public class ClassFieldSet extends Expression {
    private final RawTypeDesc owner;
    private final String fieldName;
    private final Expression value;

    public ClassFieldSet(RawTypeDesc owner, String fieldName, Expression value) {
        this.owner = owner;
        this.fieldName = fieldName;
        this.value = value;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return value.inferType(ctx);
    }

    public CodeTree compile(CodeRCtx ctx) {
        rst.TypeDef ownerType = ctx.resolve(owner);
        int fieldIdx = ownerType.getStaticFieldIndex(fieldName);
        return new CodeTree(
                value.compile(ctx),
                Opcodes.PUT_FIELD,
                ctx.getTypeIndex(owner),
                fieldIdx,
                new ClassFieldGet(owner, fieldName).compile(ctx));
    }

    public String toString() {
        return String.format("%s.%s = %s", owner, fieldName, value);
    }
}

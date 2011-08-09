package rst.exp;

import common.FullTypeDesc;
import common.NormalFullTypeDesc;
import comp.CodeTree;
import rctx.CodeRCtx;
import vm.Opcodes;

import static util.StringUtils.implode;

public class NewObject extends Expression {
    private final NormalFullTypeDesc type;
    private final Expression[] args;
    
    public NewObject(NormalFullTypeDesc type, Expression[] args) {
        this.type = type;
        this.args = args;
    }

    public FullTypeDesc inferType(CodeRCtx ctx) {
        return type;
    }

    public CodeTree compile(CodeRCtx ctx) {
        return new CodeTree(
                Opcodes.NEW, ctx.getTypeIndex(type.raw), Opcodes.DUP,
                new ClassMethodInvocation(type.raw, "init", FullTypeDesc.none, args).compile(ctx)
        );
    }

    public String toString() {
        return String.format("new %s(%s)", type, implode(", ", args));
    }
}

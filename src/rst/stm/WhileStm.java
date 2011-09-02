package rst.stm;

import comp.CodeTree;

import rctx.CodeRCtx;
import rst.exp.Expression;
import vm.Opcodes;

import static util.StringUtils.indent;

public class WhileStm extends Statement {
    private final Expression cond;
    private final Statement body;

    public WhileStm(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    public CompilationResult compile(CodeRCtx ctx) {
        CodeTree condCode = cond.compile(ctx);
        CodeTree bodyCode = body.compile(ctx).code;
        return new CompilationResult(
                new CodeTree(condCode, Opcodes.BOOL_NEG,
                        Opcodes.JUMP_COND, bodyCode.size() + 2,
                        body.compile(ctx).code,
                        Opcodes.JUMP, -(bodyCode.size() + condCode.size() + 5)),
                ctx);
    }

    public String toString() {
        if (body instanceof BlockStm)
            return String.format("while (%s) %s", cond, body);
        return String.format("while (%s)\n%s", cond, indent(body));
    }
}

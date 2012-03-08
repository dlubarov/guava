package c.stm;

import static util.StringUtils.indent;

import c.*;
import c.exp.Expression;
import c.ty.Type;
import d.Opcodes;

public class While extends Statement {
    public final Expression cond;
    public final Statement body;

    public While(Expression cond, Statement body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public CompilationResult compile(CodeContext ctx) {
        CodeTree condCode = cond.compile(Type.coreBool, ctx);
        CodeTree bodyCode = body.compile(ctx).code;
        CodeTree code = new CodeTree(
                condCode, Opcodes.BOOL_NEG,
                Opcodes.JUMP_COND, bodyCode.getSize() + 2,
                bodyCode,
                Opcodes.JUMP, -(bodyCode.getSize() + 5 + condCode.getSize())
        );
        return new CompilationResult(code, ctx);
    }

    @Override
    public String toString() {
        if (body instanceof Block)
            return String.format("while (%s) %s", cond, body);
        return String.format("while (%s)\n%s", cond, indent(body));
    }
}

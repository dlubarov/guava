package c.stm;

import static util.StringUtils.indent;

import c.*;
import c.exp.Expression;
import c.ty.Type;
import d.Opcodes;

public class IfElse extends Statement {
    public final Expression cond;
    public final Statement bodyTrue, bodyFalse;

    public IfElse(Expression cond, Statement bodyTrue, Statement bodyFalse) {
        this.cond = cond;
        this.bodyTrue = bodyTrue;
        this.bodyFalse = bodyFalse;
    }

    @Override
    public CompilationResult compile(CodeContext ctx) {
        CodeTree condCode = cond.compile(Type.coreBool, ctx);
        CodeTree trueCode = bodyTrue.compile(ctx).code;
        CodeTree falseCode = bodyFalse.compile(ctx).code;
        CodeTree code = new CodeTree(
                condCode,
                Opcodes.JUMP_COND, falseCode.getSize() + 2,
                falseCode, Opcodes.JUMP, trueCode.getSize(),
                trueCode
        );
        return new CompilationResult(code, ctx);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(cond).append(')');
        if (bodyTrue instanceof Block)
            sb.append(' ').append(bodyTrue).append(' ');
        else
            sb.append('\n').append(indent(bodyTrue)).append('\n');

        sb.append("else");
        if (bodyFalse instanceof Block)
            sb.append(' ').append(bodyFalse);
        else if (bodyFalse instanceof IfElse)
            sb.append(' ').append(bodyFalse);
        else
            sb.append('\n').append(indent(bodyFalse));

        return sb.toString();
    }
}

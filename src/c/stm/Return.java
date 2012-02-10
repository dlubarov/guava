package c.stm;

import common.NiftyException;

import c.*;
import c.exp.Expression;
import d.Opcodes;

public class Return extends Statement {
    public final Expression value;

    public Return(Expression value) {
        this.value = value;
    }

    @Override
    public CompilationResult compile(CodeContext ctx) {
        if (!value.hasType(ctx.method.returnType, ctx))
            throw new NiftyException(
                    "Return value '%s' does not conform to method's return type, %s.",
                    value, ctx.method.returnType);

        return new CompilationResult(
                new CodeTree(value.compile(ctx), Opcodes.RETURN),
                ctx);
    }

    @Override
    public String toString() {
        return String.format("return %s;", value);
    }
}

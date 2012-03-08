package c.stm;

import common.NiftyException;

import c.*;
import c.exp.Expression;
import c.ty.Type;
import d.Opcodes;

public class Return extends Statement {
    public final Expression value;

    public Return(Expression value) {
        this.value = value;
    }

    @Override
    public CompilationResult compile(CodeContext ctx) {
        if (value == null) {
            if (!Type.coreUnit.isSubtype(ctx.method.returnType, ctx))
                throw new NiftyException(
                        "%s must return a value of type %s; cannot have empty return.",
                        ctx.method.refineDesc(), ctx.method.returnType);
            return new CompilationResult(new CodeTree(Opcodes.RETURN_UNIT), ctx);
        }

        // compileWithTypeHint does this check for us, but it is nice
        // to have an error which is specific to return statements.
        if (!value.hasType(ctx.method.returnType, ctx))
            throw new NiftyException(
                    "Return value '%s' does not conform to method's return type, %s.",
                    value, ctx.method.returnType);

        return new CompilationResult(
                new CodeTree(
                        value.compile(ctx.method.returnType, ctx),
                        Opcodes.RETURN),
                ctx);
    }

    @Override
    public String toString() {
        if (value == null)
            return "return;";
        return String.format("return %s;", value);
    }
}

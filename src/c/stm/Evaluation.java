package c.stm;

import c.*;
import c.exp.Expression;
import d.Opcodes;

public class Evaluation extends Statement {
    public final Expression exp;

    public Evaluation(Expression exp) {
        this.exp = exp;
    }

    @Override
    public CompilationResult compile(CodeContext ctx) {
        return new CompilationResult(
                new CodeTree(exp.compile(ctx), Opcodes.POP),
                ctx);
    }

    @Override
    public String toString() {
        return exp.toString() + ";";
    }
}

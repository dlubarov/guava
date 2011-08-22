package rst.stm;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;
import comp.CodeTree;

import rctx.CodeRCtx;
import rst.exp.Expression;

public class ExpStm extends Statement {
    private final Expression exp;

    public ExpStm(Expression exp) {
        this.exp = exp;
    }

    public CompilationResult compile(CodeRCtx ctx) {
        return new CompilationResult(
                new CodeTree(exp.compile(ctx), Opcodes.POP),
                ctx);
    }

    public String toString() {
        return exp + ";";
    }
}

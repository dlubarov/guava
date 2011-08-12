package rst.stm;

import comp.CodeTree;
import rctx.CodeRCtx;

public class EmptyStm extends Statement {
    public static final EmptyStm INST = new EmptyStm();
    
    private EmptyStm() {}

    public CompilationResult compile(CodeRCtx ctx) {
        return new CompilationResult(new CodeTree(), ctx);
    }
    
    public String toString() {
        return ";";
    }
}

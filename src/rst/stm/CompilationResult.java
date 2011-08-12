package rst.stm;

import comp.CodeTree;
import rctx.CodeRCtx;

public class CompilationResult {
    public final CodeTree code;
    public final CodeRCtx newCtx;

    public CompilationResult(CodeTree code, CodeRCtx newCtx) {
        this.code = code;
        this.newCtx = newCtx;
    }
}

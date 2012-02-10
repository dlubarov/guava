package c.stm;

import c.*;

public class CompilationResult {
    public final CodeTree code;
    public final CodeContext newCtx;

    public CompilationResult(CodeTree code, CodeContext newCtx) {
        this.code = code;
        this.newCtx = newCtx;
    }
}

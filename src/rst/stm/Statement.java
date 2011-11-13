package rst.stm;

import rctx.CodeRCtx;

public abstract class Statement {
    public abstract CompilationResult compile(CodeRCtx ctx);

    public abstract String toString();
}

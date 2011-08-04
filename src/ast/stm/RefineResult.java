package ast.stm;

import ctx.CodeContext;

public class RefineResult {
    public final rst.stm.Statement stm;
    public final CodeContext newCtx;

    public RefineResult(rst.stm.Statement stm, CodeContext newCtx) {
        this.stm = stm;
        this.newCtx = newCtx;
    }
}

package ast.stm;

import ctx.CodeContext;

public class EmptyStm extends Statement {
    public static final EmptyStm INST = new EmptyStm();

    private EmptyStm() {}

    public RefineResult refine(CodeContext ctx) {
        return new RefineResult(rst.stm.EmptyStm.INST, ctx);
    }

    public String toString() {
        return ";";
    }
}

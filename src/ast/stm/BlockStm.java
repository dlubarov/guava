package ast.stm;

import ctx.CodeContext;

import static util.StringUtils.*;

public class BlockStm extends Statement {
    private final Statement[] parts;

    public BlockStm(Statement... parts) {
        this.parts = parts;
    }

    public RefineResult refine(CodeContext ctx) {
        // TODO: update context (will require making ast.stm.Statement.refine return RefineResult)
        rst.stm.Statement[] partsRef = new rst.stm.Statement[parts.length];
        CodeContext currentCtx = ctx;
        for (int i = 0; i < partsRef.length; ++i) {
            RefineResult res = parts[i].refine(currentCtx);
            partsRef[i] = res.stm;
            currentCtx = res.newCtx;
        }
        return new RefineResult(new rst.stm.BlockStm(partsRef), ctx);
    }

    public String toString() {
        if (parts.length == 0)
            return "{}";
        return "{\n" + indent(implode('\n', parts)) + "\n}";
    }
}

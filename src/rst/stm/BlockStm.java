package rst.stm;

import static util.StringUtils.*;

import comp.CodeTree;

import rctx.CodeRCtx;

public class BlockStm extends Statement {
    private final Statement[] parts;

    public BlockStm(Statement... parts) {
        this.parts = parts;
    }

    public CompilationResult compile(CodeRCtx ctx) {
        CodeRCtx originalCtx = ctx;
        CodeTree[] codeParts = new CodeTree[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            CompilationResult res = parts[i].compile(ctx);
            codeParts[i] = res.code;
            ctx = res.newCtx;
        }
        return new CompilationResult(new CodeTree((Object[]) codeParts), originalCtx);
    }

    public String toString() {
        if (parts.length == 0)
            return "{}";
        return "{\n" + indent(implode('\n', parts)) + "\n}";
    }
}

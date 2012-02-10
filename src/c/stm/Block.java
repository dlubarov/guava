package c.stm;

import static util.StringUtils.*;
import c.*;

public class Block extends Statement {
    public final Statement[] parts;

    public Block(Statement[] parts) {
        this.parts = parts;
    }

    @Override
    public CompilationResult compile(CodeContext ctx) {
        CodeContext originalCtx = ctx;
        CodeTree[] codeParts = new CodeTree[parts.length];
        for (Statement part : parts) {
            CompilationResult res = part.compile(ctx);
            ctx = res.newCtx;
        }
        return new CompilationResult(
                new CodeTree((Object[]) codeParts),
                originalCtx);
    }

    @Override
    public String toString() {
        if (parts.length == 0)
            return "{}";
        return "{\n" + indent(implode('\n', parts)) + "\n}";
    }
}

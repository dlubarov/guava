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
        for (int i = 0; i < codeParts.length; ++i) {
            CompilationResult res = parts[i].compile(ctx);
            codeParts[i] = res.code;
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

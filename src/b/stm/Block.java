package b.stm;

import static util.StringUtils.*;
import b.*;

public class Block extends Statement {
    public final Statement[] parts;

    public Block(Statement... parts) {
        this.parts = parts;
    }

    @Override
    public c.stm.Block refine(TypeDef typeCtx, MethodDef methodCtx) {
        c.stm.Statement[] refinedParts = new c.stm.Statement[parts.length];
        for (int i = 0; i < refinedParts.length; ++i)
            refinedParts[i] = parts[i].refine(typeCtx, methodCtx);
        return new c.stm.Block(refinedParts);
    }

    @Override
    public String toString() {
        if (parts.length == 0)
            return "{}";
        return "{\n" + indent(implode('\n', parts)) + "\n}";
    }
}

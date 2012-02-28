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
        return new c.stm.Block(Statement.refineAll(parts, typeCtx, methodCtx));
    }

    @Override
    public String toString() {
        if (parts.length == 0)
            return "{}";
        return "{\n" + indent(implode('\n', parts)) + "\n}";
    }
}

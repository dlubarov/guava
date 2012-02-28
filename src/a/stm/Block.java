package a.stm;

import static util.StringUtils.*;

public class Block extends Statement {
    public final Statement[] parts;

    public Block(Statement... parts) {
        this.parts = parts;
    }

    @Override
    public b.stm.Block refine() {
        return new b.stm.Block(Statement.refineAll(parts));
    }

    @Override
    public String toString() {
        if (parts.length == 0)
            return "{}";
        return "{\n" + indent(implode('\n', parts)) + "\n}";
    }
}

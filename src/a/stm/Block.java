package a.stm;

import static util.StringUtils.*;

public class Block extends Statement {
    public final Statement[] parts;

    public Block(Statement... parts) {
        this.parts = parts;
    }

    @Override
    public b.stm.Block refine() {
        b.stm.Statement[] refinedParts = new b.stm.Statement[parts.length];
        for (int i = 0; i < refinedParts.length; ++i)
            refinedParts[i] = parts[i].refine();
        return new b.stm.Block(refinedParts);
    }

    @Override
    public String toString() {
        if (parts.length == 0)
            return "{}";
        return "{\n" + indent(implode('\n', parts)) + "\n}";
    }
}

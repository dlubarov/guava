package rst.stm;

import static util.StringUtils.implode;
import static util.StringUtils.indent;

public class BlockStm extends Statement {
    private final Statement[] parts;

    public BlockStm(Statement... parts) {
        this.parts = parts;
    }

    public String toString() {
        if (parts.length == 0)
            return "{}";
        return "{\n" + indent(implode('\n', parts)) + "\n}";
    }
}

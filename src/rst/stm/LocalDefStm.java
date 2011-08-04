package rst.stm;

import common.FullTypeDesc;
import common.RawTypeDesc;
import rst.exp.Expression;

public class LocalDefStm extends Statement {
    private final FullTypeDesc type;
    private final int index;
    private final Expression initVal;

    public LocalDefStm(FullTypeDesc type, int index, Expression initVal) {
        this.type = type;
        this.index = index;
        this.initVal = initVal;
    }

    public String toString() {
        if (initVal == null)
            return String.format("%s local_%d;", type, index);
        return String.format("%s local_%d = %s;", type, index, initVal);
    }
}

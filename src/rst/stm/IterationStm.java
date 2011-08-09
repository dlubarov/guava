package rst.stm;

import common.FullTypeDesc;
import rst.exp.Expression;

import static util.StringUtils.indent;

public class IterationStm extends Statement {
    private final FullTypeDesc varType;
    private final int varIndex;
    private final Expression iterable;
    private final Statement body;
    
    public IterationStm(FullTypeDesc varType, int varIndex, Expression iterable, Statement body) {
        this.varType = varType;
        this.varIndex = varIndex;
        this.iterable = iterable;
        this.body = body;
    }
    
    public String toString() {
        String loop = String.format("for (%s local_%d : %s)",
                varType, varIndex, iterable);
        if (body instanceof BlockStm)
            return loop + " " + body;
        return loop + "\n" + indent(body);
    }
}

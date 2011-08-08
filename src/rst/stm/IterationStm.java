package rst.stm;

import common.FullTypeDesc;

public class IterationStm extends Statement {
    private final FullTypeDesc varType;
    private final int varIndex;
    private final Statement body;
    
    public IterationStm(FullTypeDesc varType, int varIndex, Statement body) {
        this.varType = varType;
        this.varIndex = varIndex;
        this.body = body;
    }
    
    public String toString() {
        return String.format("for (%a : %s)\n%s", body);
    }
}

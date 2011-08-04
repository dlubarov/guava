package rst;

import common.FullTypeDesc;
import rst.exp.Expression;

public class FieldDef {
    public final FullTypeDesc type;
    public final String name;
    private final Expression initVal;
    private final boolean isStatic, isReadOnly;

    public FieldDef(FullTypeDesc type, String name, Expression initVal,
                    boolean isStatic, boolean isReadOnly) {
        this.type = type;
        this.name = name;
        this.initVal = initVal;
        this.isStatic = isStatic;
        this.isReadOnly = isReadOnly;
    }
    
    public String toString() {
        if (initVal == null)
            return String.format("%s %s;", type, name);
        return String.format("%s %s = %s;", type, name, initVal);
    }
}

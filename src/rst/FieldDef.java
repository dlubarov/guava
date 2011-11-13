package rst;

import common.FullTypeDesc;

public class FieldDef {
    public final FullTypeDesc type;
    public final String name;
    private final boolean isStatic, isReadOnly;

    public FieldDef(FullTypeDesc type, String name,
                    boolean isStatic, boolean isReadOnly) {
        this.type = type;
        this.name = name;
        this.isStatic = isStatic;
        this.isReadOnly = isReadOnly;
    }

    public String toString() {
        return String.format("%s %s;", type, name);
    }
}

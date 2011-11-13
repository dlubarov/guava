package rst;

import common.*;

public class GenericInfo {
    public final Variance var;
    public final FullTypeDesc[] parentTypes;
    public final FullTypeDesc[] childTypes;

    public GenericInfo(Variance var, FullTypeDesc[] parentTypes, FullTypeDesc[] childTypes) {
        this.var = var;
        this.parentTypes = parentTypes;
        this.childTypes = childTypes;
    }
}

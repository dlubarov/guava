package a.gen;

import common.Variance;

public class TypeGenericParam {
    public final Variance var;
    public final String name;

    public TypeGenericParam(Variance var, String name) {
        this.var = var;
        this.name = name;
    }

    @Override
    public String toString() {
        return var + name;
    }
}

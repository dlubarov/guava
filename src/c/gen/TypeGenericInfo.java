package c.gen;

import common.Variance;

import c.ty.Type;

public class TypeGenericInfo extends GenericInfo {
    public final Variance var;

    public TypeGenericInfo(Variance var, int index, Type[] subOf, Type[] supOf) {
        super(index, subOf, supOf);
        this.var = var;
    }

    @Override
    public String toString() {
        return var + "T" + super.toString();
    }
}

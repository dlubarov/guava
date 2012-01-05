package b.gen;

import b.*;
import common.Variance;

public class TypeGenericParam extends GenericParam {
    public final Variance var;

    public TypeGenericParam(Variance var, String genericParam, Type[] subOf, Type[] supOf) {
        super(genericParam, subOf, supOf);
        this.var = var;
    }

    public c.gen.TypeGenericInfo refine(TypeDef typeCtx, int index) {
        c.ty.Type[] refinedSubOf = new c.ty.Type[subOf.length],
                    refinedSupOf = new c.ty.Type[supOf.length];
        for (int i = 0; i < refinedSubOf.length; ++i)
            refinedSubOf[i] = subOf[i].refine(typeCtx, null);
        for (int i = 0; i < refinedSupOf.length; ++i)
            refinedSupOf[i] = supOf[i].refine(typeCtx, null);
        return new c.gen.TypeGenericInfo(var, index, refinedSubOf, refinedSupOf);
    }

    @Override
    public String toString() {
        return var + super.toString();
    }
}

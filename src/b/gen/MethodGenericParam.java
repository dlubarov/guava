package b.gen;

import b.*;

public class MethodGenericParam extends GenericParam {
    public MethodGenericParam(String name, Type[] subOf, Type[] supOf) {
        super(name, subOf, supOf);
    }

    public c.gen.MethodGenericInfo refine(TypeDef typeCtx, MethodDef methodCtx, int index) {
        c.ty.Type[] refinedSubOf = new c.ty.Type[subOf.length],
                    refinedSupOf = new c.ty.Type[supOf.length];
        for (int i = 0; i < refinedSubOf.length; ++i)
            refinedSubOf[i] = subOf[i].refine(typeCtx, methodCtx);
        for (int i = 0; i < refinedSupOf.length; ++i)
            refinedSupOf[i] = supOf[i].refine(typeCtx, methodCtx);
        return new c.gen.MethodGenericInfo(index, refinedSubOf, refinedSupOf);
    }
}

package d.ty.desc;

import d.TypeDef;
import d.ty.nf.*;
import d.ty.sup.*;

public class MethodGenericTypeDesc extends TypeDesc {
    public final int index;

    public MethodGenericTypeDesc(int index) {
        this.index = index;
    }

    @Override
    public NonFinalType toNonFinal(TypeDef owner) {
        return new MethodGenericNonFinalType(index);
    }

    @Override
    public SuperType toSuper() {
        throw new UnsupportedOperationException("method generic cannot be converted to SuperType");
    }

    @Override
    public boolean equals(Object o) {
        try {
            return index == ((MethodGenericTypeDesc) o).index;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return "M" + index;
    }
}

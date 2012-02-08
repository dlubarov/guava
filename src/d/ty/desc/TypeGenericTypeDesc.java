package d.ty.desc;

import d.TypeDef;
import d.ty.nf.*;
import d.ty.sup.*;

public class TypeGenericTypeDesc extends TypeDesc {
    public final int index;

    public TypeGenericTypeDesc(int index) {
        this.index = index;
    }

    @Override
    public NonFinalType toNonFinal(TypeDef owner) {
        return new TypeGenericNonFinalType(owner, index);
    }

    @Override
    public SuperType toSuper() {
        return new TypeGenericSuperType(index);
    }

    @Override
    public boolean equals(Object o) {
        try {
            return index == ((TypeGenericTypeDesc) o).index;
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
        return "T" + index;
    }
}

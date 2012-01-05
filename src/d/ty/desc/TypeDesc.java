package d.ty.desc;

import d.TypeDef;
import d.ty.nf.NonFinalType;
import d.ty.sup.SuperType;

public abstract class TypeDesc {
    public static final TypeDesc[] NONE = new TypeDesc[0];

    public abstract NonFinalType toNonFinal(TypeDef owner);

    public abstract SuperType toSuper();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}

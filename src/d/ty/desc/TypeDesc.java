package d.ty.desc;

import common.RawType;

import d.TypeDef;
import d.ty.nf.NonFinalType;
import d.ty.sup.SuperType;

public abstract class TypeDesc {
    public static final TypeDesc[] NONE = new TypeDesc[0];

    public static final TypeDesc
            coreTop = new ParameterizedTypeDesc(RawType.coreTop),
            coreBottom = new ParameterizedTypeDesc(RawType.coreBottom),
            coreInt = new ParameterizedTypeDesc(RawType.coreInt),
            coreLong = new ParameterizedTypeDesc(RawType.coreLong),
            coreByte = new ParameterizedTypeDesc(RawType.coreByte),
            coreDouble = new ParameterizedTypeDesc(RawType.coreDouble),
            coreChar = new ParameterizedTypeDesc(RawType.coreChar),
            coreBool = new ParameterizedTypeDesc(RawType.coreBool),
            coreString = new ParameterizedTypeDesc(RawType.coreString);

    public static final TypeDesc[]
            coreTopOnly = new TypeDesc[] {coreTop},
            coreBottomOnly = new TypeDesc[] {coreBottom},
            coreIntOnly = new TypeDesc[] {coreInt},
            coreLongOnly = new TypeDesc[] {coreLong},
            coreByteOnly = new TypeDesc[] {coreByte},
            coreDoubleOnly = new TypeDesc[] {coreDouble},
            coreCharOnly = new TypeDesc[] {coreChar},
            coreBoolOnly = new TypeDesc[] {coreBool},
            coreStringOnly = new TypeDesc[] {coreString};

    public abstract NonFinalType toNonFinal(TypeDef owner);

    public abstract SuperType toSuper();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}

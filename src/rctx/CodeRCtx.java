package rctx;

import common.FullTypeDesc;
import common.RawMethodDesc;
import common.RawTypeDesc;
import rst.TypeDef;

public class CodeRCtx {
    public TypeDef resolve(RawTypeDesc desc) {
        throw new UnsupportedOperationException("FIXME: impl");
    }

    public FullTypeDesc getLocalType(int index) {
        throw new UnsupportedOperationException("FIXME: impl");
    }

    public int getTypeIndex(RawTypeDesc desc) {
        throw new UnsupportedOperationException("FIXME: impl");
    }

    public int getMethodIndex(RawMethodDesc desc) {
        throw new UnsupportedOperationException("FIXME: impl");
    }
}

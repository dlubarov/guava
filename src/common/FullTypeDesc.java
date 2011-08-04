package common;

import rctx.CodeRCtx;

public abstract class FullTypeDesc {
    public static final FullTypeDesc[] none = new FullTypeDesc[0];
    
    public abstract FullTypeDesc withTypeGenerics(FullTypeDesc[] typeGenerics);
    public abstract FullTypeDesc withMethodGenerics(FullTypeDesc[] methodGenerics);

    public abstract boolean isSubtype(FullTypeDesc that, CodeRCtx ctx);
    public abstract boolean isSupertype(FullTypeDesc that, CodeRCtx ctx);
}

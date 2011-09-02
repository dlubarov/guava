package common;

import rctx.CodeRCtx;

public class TypeGenericFullTypeDesc extends GenericFullTypeDesc {
    // TODO: Can we get rid of owner? Would fix hack in MethodContext.
    // See also MethodGenericFullTypeDesc
    private final RawTypeDesc owner;
    private final int index;
    
    public TypeGenericFullTypeDesc(RawTypeDesc owner, int index) {
        this.owner = owner;
        this.index = index;
    }
    
    public FullTypeDesc withTypeGenerics(FullTypeDesc[] typeGenerics) {
        try {
            return typeGenerics[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("too few generic args for " + owner);
        }
    }
    
    public FullTypeDesc withMethodGenerics(FullTypeDesc[] methodGenerics) {
        return this;
    }
    
    public boolean isSubtype(FullTypeDesc that, CodeRCtx ctx) {
        // TODO: generic upper bounds
        return equals(that);
    }
    
    public boolean isSupertype(FullTypeDesc that, CodeRCtx ctx) {
        // TODO: generic lower bounds
        return equals(that);
    }
    
    @Override
    public boolean equals(Object o) {
        try {
            TypeGenericFullTypeDesc that = (TypeGenericFullTypeDesc) o;
            return owner.equals(that.owner) && index == that.index;
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return owner.hashCode() * 31 + index;
    }
    
    public String toString() {
        return String.format("T%d", index);
    }
}

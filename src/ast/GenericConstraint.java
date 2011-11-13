package ast;

public final class GenericConstraint {
    public final String genericArg;
    public final GenericConstraintRel rel;
    public final Type that;

    public GenericConstraint(String genericArg, GenericConstraintRel rel, Type that) {
        this.genericArg = genericArg;
        this.rel = rel;
        this.that = that;
    }

    public String toString() {
        return String.format("%s %s %s", genericArg, rel, that);
    }
}

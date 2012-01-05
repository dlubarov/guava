package a.gen;

import a.*;

public class GenericConstraint {
    public final String genericParam;
    public final GenericConstraintRel rel;
    public final Type otherType;

    public GenericConstraint(String genericParam, GenericConstraintRel rel, Type otherType) {
        this.genericParam = genericParam;
        this.rel = rel;
        this.otherType = otherType;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", genericParam, rel, otherType);
    }
}

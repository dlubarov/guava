package a.gen;

public enum GenericConstraintRel {
    SUBTYPE("<"), SUPERTYPE(">");

    private final String s;

    private GenericConstraintRel(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}

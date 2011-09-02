package ast;

public enum GenericConstraintRel {
    SUBTYPE("<"), SUPTYPE(">");
    
    private final String s;
    
    GenericConstraintRel(String s) {
        this.s = s;
    }
    
    public String toString() {
        return s;
    }
}

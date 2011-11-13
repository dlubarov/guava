package ast.exp;

public enum PrefixOp {
    // Must be ordered by length!
    PRE_INC("++"), PRE_DEC("--"),
    AFFIRM("+"), NEGATE("-"),
    TILDE("~"), BANG("!");

    private final String s;

    private PrefixOp(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }
}

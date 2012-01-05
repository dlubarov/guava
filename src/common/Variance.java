package common;

public enum Variance {
    COVARIANT("+"), NONVARIANT(""), CONTRAVARIANT("-");

    private final String s;

    private Variance(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}

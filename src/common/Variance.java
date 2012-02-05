package common;

public enum Variance {
    COVARIANT("+"), NONVARIANT(""), CONTRAVARIANT("-");

    public static Variance[] NONE = new Variance[0];

    private final String s;

    private Variance(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}

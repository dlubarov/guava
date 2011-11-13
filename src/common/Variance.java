package common;

public enum Variance {
    COVARIANT("+"),
    NONVARIANT(""),
    CONTRAVARIANT("-");

    private final String repr;

    private Variance(String repr) {
        this.repr = repr;
    }

    public String toString() {
        return repr;
    }
}

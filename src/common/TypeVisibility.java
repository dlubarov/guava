package common;

public enum TypeVisibility {
    PUBLIC("public "), MODULE("");

    private final String s;

    private TypeVisibility(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}

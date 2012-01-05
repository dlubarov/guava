package common;

public enum FieldVisibility {
    PUBLIC("public "), MODULE(""), PRIVATE("private ");

    private final String s;

    private FieldVisibility(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}

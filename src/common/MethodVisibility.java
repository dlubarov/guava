package common;

public enum MethodVisibility {
    PUBLIC("public "), MODULE(""), PRIVATE("private ");

    private final String s;

    private MethodVisibility(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}

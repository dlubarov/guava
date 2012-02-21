package common;

public enum MethodVisibility {
    PUBLIC("public "), MODULE(""), PRIVATE("private ");

    private final String s;

    private MethodVisibility(String s) {
        this.s = s;
    }

    public boolean lessAccessible(MethodVisibility that) {
        return ordinal() > that.ordinal();
    }

    @Override
    public String toString() {
        return s;
    }
}

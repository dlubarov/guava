package vm;

public abstract class TObject {
    private final Type type;

    public TObject(Type type) {
        this.type = type;
    }
}

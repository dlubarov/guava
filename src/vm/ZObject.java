package vm;

public abstract class ZObject {
    public final Type type;

    public ZObject(Type type) {
        this.type = type;
    }
}

package vm;

public class NormalObject extends ZObject {
    public final ZObject[] fields;

    public NormalObject(NormalType type) {
        super(type);
        fields = new ZObject[type.numFields];
    }
}

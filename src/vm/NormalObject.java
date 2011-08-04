package vm;

public class NormalObject extends TObject {
    private final TObject[] fields;

    public NormalObject(NormalType type) {
        super(type);
        fields = new TObject[type.numFields];
    }
}

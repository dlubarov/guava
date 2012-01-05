package a;

public class Import {
    // A null type corresponds to importing *
    public final String module, type;

    public Import(String module, String type) {
        this.module = module;
        this.type = type;
    }

    @Override
    public String toString() {
        if (type == null)
            return String.format("import %s.*;", module);
        return String.format("import %s.%s;", module, type);
    }
}

package b.imp;

public class SpecificImport extends Import {
    public final String module, type;

    public SpecificImport(String module, String type) {
        this.module = module;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("import %s.%s;", module, type);
    }
}

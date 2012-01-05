package b.imp;

public class WildcardImport extends Import {
    public final String module;

    public WildcardImport(String module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return String.format("import %s.*;", module);
    }
}

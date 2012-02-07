package b.imp;

public class WildcardImport extends Import {
    public final String module;

    public WildcardImport(String module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WildcardImport && module.equals(((WildcardImport) o).module);
    }

    @Override
    public int hashCode() {
        return module.hashCode();
    }

    @Override
    public String toString() {
        return String.format("import %s.*;", module);
    }
}

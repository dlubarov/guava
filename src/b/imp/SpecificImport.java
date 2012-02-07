package b.imp;

public class SpecificImport extends Import {
    public final String module, type;

    public SpecificImport(String module, String type) {
        this.module = module;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        try {
            SpecificImport that = (SpecificImport) o;
            return module.equals(that.module) && type.equals(that.type);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return String.format("import %s.%s;", module, type);
    }
}

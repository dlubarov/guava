package common;

public class RawTypeDesc {
    public final String module;
    public final String name;

    public RawTypeDesc(String module, String name) {
        this.module = module;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        try {
            RawTypeDesc that = (RawTypeDesc) o;
            return module.equals(that.module) && name.equals(that.name);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return module.hashCode() * 31 + name.hashCode();
    }
    
    public String toString() {
        return String.format("%s.%s", module, name);
    }
}

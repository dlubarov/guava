package ast;

public class Import {
    public final String module;
    public final String type;

    public Import(String module, String type) {
        this.module = module;
        this.type = type;
    }

    public String toString() {
        return String.format("import %s.%s;", module, type == null? "*" : type);
    }
}

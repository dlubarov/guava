package ast;

public class TypedVar {
    public final Type type;
    public final String name;

    public TypedVar(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public String toString() {
        return String.format("%s %s", type, name);
    }
}

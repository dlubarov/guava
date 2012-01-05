package a.exp;

public class Variable extends Expression {
    public final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public b.exp.Expression refine() {
        return new b.exp.Variable(name);
    }

    @Override
    public String toString() {
        return name;
    }
}

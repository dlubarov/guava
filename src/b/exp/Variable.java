package b.exp;

import b.*;

public class Variable extends Expression {
    public final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public c.exp.Expression refine(TypeDef typeCtx, MethodDef methodCtx) {
        return new c.exp.LocalGet(name);
    }

    @Override
    public String toString() {
        return name;
    }
}

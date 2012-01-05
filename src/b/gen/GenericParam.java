package b.gen;

import static util.StringUtils.implode;
import b.*;

public abstract class GenericParam {
    public final String name;
    public final Type[] subOf, supOf;

    public GenericParam(String name, Type[] subOf, Type[] supOf) {
        this.name = name;
        this.subOf = subOf;
        this.supOf = supOf;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        if (subOf.length > 0)
            sb.append(String.format(" sub {%s}", implode(", ", subOf)));
        if (supOf.length > 0)
            sb.append(String.format(" sup {%s}", implode(", ", supOf)));
        return sb.toString();
    }
}

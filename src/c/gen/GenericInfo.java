package c.gen;

import static util.StringUtils.implode;
import c.ty.Type;

public abstract class GenericInfo {
    public final int index;
    public final Type[] subOf, supOf;

    public GenericInfo(int index, Type[] subOf, Type[] supOf) {
        this.index = index;
        this.subOf = subOf;
        this.supOf = supOf;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(Integer.toString(index));
        if (subOf.length > 0)
            sb.append(String.format(" sub {%s}", implode(", ", subOf)));
        if (supOf.length > 0)
            sb.append(String.format(" sup {%s}", implode(", ", supOf)));
        return sb.toString();
    }
}

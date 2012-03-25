package a.exp.lit;

import util.StringUtils;
import a.exp.Expression;

public class LiteralString extends Expression {
    public final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    @Override
    public b.exp.Expression refine() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c == '\\')
                c = StringUtils.unescape(value.charAt(++i));
            sb.append(c);
        }
        return new b.exp.lit.LiteralString(sb.toString());
    }

    @Override
    public String toString() {
        return '"' + value + '"';
    }
}

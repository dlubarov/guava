package a.exp;

import common.NiftyException;

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
                switch (c = value.charAt(++i)) {
                    case 'r': c = '\r'; break;
                    case 'n': c = '\n'; break;
                    case 't': c = '\t'; break;
                    case '0': c = '\0'; break;
                    case '"': case '\\': break;
                    default: throw new NiftyException("can't escape character '%c'", c);
                }
            sb.append(c);
        }
        return new b.exp.LiteralString(sb.toString());
    }

    @Override
    public String toString() {
        return '"' + value + '"';
    }
}

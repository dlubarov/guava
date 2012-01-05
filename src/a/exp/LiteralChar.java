package a.exp;

import common.NiftyException;

public class LiteralChar extends Expression {
    public final String value;

    public LiteralChar(String value) {
        this.value = value;
    }

    @Override
    public b.exp.Expression refine() {
        if (value.isEmpty())
            throw new NiftyException("empty character literal");
        if (value.length() == 1)
            return new b.exp.LiteralChar(value.charAt(0));
        if (value.length() == 2 && value.charAt(0) == '\\') {
            char c = value.charAt(1);
            switch (c) {
                case 'r': c = '\r'; break;
                case 'n': c = '\n'; break;
                case 't': c = '\t'; break;
                case '0': c = '\0'; break;
                case '\'': case '\\': break;
                default: throw new NiftyException("can't escape character '%c'", c);
            }
        }
        throw new NiftyException("more than one character in character literal");
    }

    @Override
    public String toString() {
        return String.format("'%s'", value);
    }
}

package a.exp.lit;

import util.StringUtils;
import a.exp.Expression;
import common.NiftyException;

public class LiteralChar extends Expression {
    public final String value;

    public LiteralChar(String value) {
        this.value = value;
    }

    @Override
    public b.exp.Expression refine() {
        switch (value.length()) {
            case 0:
                throw new NiftyException("Empty character literal.");
            case 1:
                if (value.charAt(0) == '\\')
                    throw new NiftyException("Bad character literal %s; backslash needs to be escaped.", this);
                return new b.exp.lit.LiteralChar(value.charAt(0));
            case 2:
                if (value.charAt(0) == '\\')
                    return new b.exp.lit.LiteralChar(StringUtils.unescape(value.charAt(1)));
            default:
                throw new NiftyException("More than one character in character literal.");
        }
    }

    @Override
    public String toString() {
        return String.format("'%s'", value);
    }
}

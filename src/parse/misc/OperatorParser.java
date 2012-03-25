package parse.misc;

import parse.*;

import util.StringUtils;

public class OperatorParser extends Parser<String> {
    public static final Parser<String> singleton = new OperatorParser().memo();
    private OperatorParser() {}

    public static final String[] operators;

    static {
        operators = new String[] {
                "|=", "^=", "&=",
                "<<=", ">>>=", ">>=",
                "+=", "-=",
                "*=", "/=", "%=",
                "**=",

                "~", "!",
                "|", "^", "&",
                "==", "!=",
                "<=", ">=", "<", ">",
                "<<", ">>>", ">>",
                ":+", "+:",
                "+", "-",
                "*", "/", "%",
                "**"
        };
        StringUtils.sortDescendingLength(operators);
    }

    @Override
    public Success<String> parse(String s, int p) {
        for (String op : operators)
            if (StringUtils.containsAt(s, op, p))
                return new Success<String>(op, p + op.length());
        return null;
    }
}

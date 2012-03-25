package parse.exp.lit;

import a.exp.*;
import a.exp.lit.LiteralString;
import parse.*;

public class LiteralStringParser extends Parser<Expression> {
    public static final LiteralStringParser singleton = new LiteralStringParser();
    private LiteralStringParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        if (s.charAt(p++) != '"')
            return null;
        StringBuilder sb = new StringBuilder();
        try {
            for (;;) {
                char c = s.charAt(p++);
                if (c == '"')
                    return new Success<Expression>(new LiteralString(sb.toString()), p);
                sb.append(c);
                if (c == '\\')
                    sb.append(s.charAt(p++));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("unclosed string literal");
        }
    }
}

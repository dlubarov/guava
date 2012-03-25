package parse.exp.lit;

import a.exp.*;
import a.exp.lit.LiteralChar;
import parse.*;

public class LiteralCharParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new LiteralCharParser();
    private LiteralCharParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        if (s.charAt(p++) != '\'')
            return null;
        StringBuilder sb = new StringBuilder();
        try {
            for (;;) {
                char c = s.charAt(p++);
                if (c == '\'')
                    return new Success<Expression>(new LiteralChar(sb.toString()), p);
                sb.append(c);
                if (c == '\\')
                    sb.append(s.charAt(p++));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("unclosed character literal");
        }
    }
}

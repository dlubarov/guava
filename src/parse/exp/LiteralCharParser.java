package parse.exp;

import a.exp.LiteralChar;
import parse.*;

public class LiteralCharParser extends Parser<LiteralChar> {
    public static final LiteralCharParser singleton = new LiteralCharParser();
    private LiteralCharParser() {}

    @Override
    public Success<LiteralChar> parse(String s, int p) {
        if (s.charAt(p++) != '\'')
            return null;
        StringBuilder sb = new StringBuilder();
        try {
            for (;;) {
                char c = s.charAt(p++);
                if (c == '\'')
                    return new Success<LiteralChar>(new LiteralChar(sb.toString()), p);
                sb.append(c);
                if (c == '\\')
                    sb.append(s.charAt(p++));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("unclosed character literal");
        }
    }
}

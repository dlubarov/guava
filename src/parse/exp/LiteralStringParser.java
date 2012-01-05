package parse.exp;

import a.exp.LiteralString;
import parse.*;

public class LiteralStringParser extends Parser<LiteralString> {
    public static final LiteralStringParser singleton = new LiteralStringParser();
    private LiteralStringParser() {}

    @Override
    public Success<LiteralString> parse(String s, int p) {
        if (s.charAt(p++) != '"')
            return null;
        StringBuilder sb = new StringBuilder();
        try {
            for (;;) {
                char c = s.charAt(p++);
                if (c == '"')
                    return new Success<LiteralString>(new LiteralString(sb.toString()), p);
                sb.append(c);
                if (c == '\\')
                    sb.append(s.charAt(p++));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("unclosed string literal");
        }
    }
}

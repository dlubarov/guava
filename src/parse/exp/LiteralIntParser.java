package parse.exp;

import a.exp.LiteralInt;
import parse.*;

public class LiteralIntParser extends Parser<LiteralInt> {
    public static final LiteralIntParser singleton = new LiteralIntParser();
    private LiteralIntParser() {}

    @Override
    public Success<LiteralInt> parse(String s, int p) {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(s.charAt(p)))
            sb.append(s.charAt(p++));
        if (sb.length() == 0)
            return null;
        try {
            int n = Integer.parseInt(sb.toString());
            return new Success<LiteralInt>(new LiteralInt(n), p);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Literal int is too large");
        }
    }
}

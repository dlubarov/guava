package parse.exp;

import common.NiftyException;

import a.exp.*;
import parse.*;

public class LiteralIntParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new LiteralIntParser();
    private LiteralIntParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        StringBuilder sb = new StringBuilder();

        // Read in digits until there are none left.
        while (Character.isDigit(s.charAt(p)))
            sb.append(s.charAt(p++));

        // If we didn't find any digits, this ain't an int.
        if (sb.length() == 0)
            return null;

        try {
            int n = Integer.parseInt(sb.toString());
            return new Success<Expression>(new LiteralInt(n), p);
        } catch (NumberFormatException e) {
            throw new NiftyException("Literal int %s is too large.", sb);
        }
    }
}

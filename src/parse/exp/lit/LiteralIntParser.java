package parse.exp.lit;

import common.NiftyException;

import a.exp.*;
import a.exp.lit.*;
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

        // See if this has a long literal suffix.
        boolean isLong = s.charAt(p) == 'l' || s.charAt(p) == 'L';
        if (isLong)
            ++p;

        if (isLong)
            try {
                long n = Long.parseLong(sb.toString());
                return new Success<Expression>(new LiteralLong(n), p);
            } catch (NumberFormatException e) {
                throw new NiftyException("Literal Long '%s' is too large.", sb);
            }

        try {
            int n = Integer.parseInt(sb.toString());
            return new Success<Expression>(new LiteralInt(n), p);
        } catch (NumberFormatException e) {
            throw new NiftyException("Literal Int '%s' is too large.", sb);
        }
    }
}

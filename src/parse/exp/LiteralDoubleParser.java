package parse.exp;

import common.NiftyException;

import a.exp.*;
import parse.*;

// TODO: Add exponent syntax for literal doubles.

public class LiteralDoubleParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new LiteralDoubleParser();
    private LiteralDoubleParser() {}

    private static Success<String> parseDigits(String s, int p) {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(s.charAt(p)))
            sb.append(s.charAt(p++));
        return new Success<String>(sb.toString(), p);
    }

    @Override
    public Success<Expression> parse(String s, int p) {
        // Parse digits before the decimal point.
        Success<String> resLeft = parseDigits(s, p);
        p = resLeft.rem;

        // Parse the decimal point.
        if (s.charAt(p++) != '.')
            return null;

        // Parse digits after the decimal point.
        Success<String> resRight = parseDigits(s, p);
        if (resRight.value.isEmpty())
            return null; // Don't allow e.g. "2.", mainly for ease of parsing.
        p = resRight.rem;

        String combined = resLeft.value + '.' + resRight.value;
        try {
            double x = Double.parseDouble(combined);
            return new Success<Expression>(new LiteralDouble(x), p);
        } catch (NumberFormatException e) {
            throw new NiftyException("Failed to parse double '%s', reason unknown.", combined);
        }
    }
}

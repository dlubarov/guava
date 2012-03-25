package parse.exp.lit;

import a.exp.*;
import a.exp.lit.LiteralDouble;
import parse.*;

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
        // Parse a string of digits before and after the decimal point.
        Success<String> resA = parseDigits(s, p);
        p = resA.rem;
        if (s.charAt(p++) != '.')
            return null;
        Success<String> resB = parseDigits(s, p);
        p = resB.rem;

        // Put them together.
        String a = resA.value, b = resB.value;
        if (a.isEmpty() && b.isEmpty())
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append(a).append('.').append(b);

        // Parse the exponent.
        if (Character.isLowerCase(s.charAt(p))) {
            sb.append(s.charAt(p++));
            if (s.charAt(p) == '-') {
                ++p;
                sb.append('-');
            }
            Success<String> resExp = parseDigits(s, p);
            if (resExp.value.isEmpty())
                return null;
            sb.append(resExp.value);
        }

        Expression Success = new LiteralDouble(Double.parseDouble(sb.toString()));
        return new Success<Expression>(Success, p);
    }
}

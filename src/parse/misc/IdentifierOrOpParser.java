package parse.misc;

import parse.*;

public class IdentifierOrOpParser extends Parser<String> {
    public static final Parser<String> singleton = new IdentifierOrOpParser().memo();
    private IdentifierOrOpParser() {}

    @Override
    public Success<String> parse(String s, int p) {
        Success<String> result = IdentifierParser.singleton.parse(s, p);
        if (result == null)
            result = OperatorParser.singleton.parse(s, p);
        return result;
    }
}

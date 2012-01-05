package parse.exp;

import a.exp.LiteralBool;
import parse.*;

public class LiteralBoolParser extends Parser<LiteralBool> {
    public static final LiteralBoolParser singleton = new LiteralBoolParser();
    private LiteralBoolParser() {}

    @Override
    public Success<LiteralBool> parse(String s, int p) {
        Success<String> res = IdentifierParser.singleton.parse(s, p);
        if (res != null) {
            if (res.value.equals("true"))
                return new Success<LiteralBool>(new LiteralBool(true), res.rem);
            if (res.value.equals("false"))
                return new Success<LiteralBool>(new LiteralBool(false), res.rem);
        }
        return null;
    }
}

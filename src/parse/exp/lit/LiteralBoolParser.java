package parse.exp.lit;

import a.exp.*;
import a.exp.lit.LiteralBool;
import parse.*;
import parse.misc.IdentifierParser;

public class LiteralBoolParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new LiteralBoolParser();
    private LiteralBoolParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        Success<String> res = IdentifierParser.singleton.parse(s, p);
        if (res != null) {
            if (res.value.equals("true"))
                return new Success<Expression>(new LiteralBool(true), res.rem);
            if (res.value.equals("false"))
                return new Success<Expression>(new LiteralBool(false), res.rem);
        }
        return null;
    }
}

package parse.exp;

import a.exp.*;
import parse.*;
import parse.misc.IdentifierParser;

public class AtomParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new AtomParser().memo();
    private AtomParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        Success<Expression> result;

        // Try to parse an parenthetical expression.
        result = ParentheticalExpressionParser.singleton.parse(s, p);
        if (result != null)
            return result;

        // Try to parse a literal Int.
        result = LiteralIntParser.singleton.parse(s, p);
        if (result != null)
            return result;

        // Try to parse a literal Bool.
        result = LiteralBoolParser.singleton.parse(s, p);
        if (result != null)
            return result;

        // Try to parse a literal Char.
        result = LiteralCharParser.singleton.parse(s, p);
        if (result != null)
            return result;

        // Try to parse a literal String.
        result = LiteralStringParser.singleton.parse(s, p);
        if (result != null)
            return result;

        // Try to parse an instantiation.
        result = InstantiationParser.singleton.parse(s, p);
        if (result != null)
            return result;

        // Try to parse a local variable.
        Success<String> resIdent = IdentifierParser.singleton.parse(s, p);
        if (resIdent != null)
            return new Success<Expression>(new Variable(resIdent.value), resIdent.rem);

        // We tried everything. There is no atomic expression here.
        return null;
    }
}

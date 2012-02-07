package parse.exp;

import a.exp.*;
import parse.*;
import parse.misc.IdentifierParser;

// Parses a variable expression (essentially just any identifier in an expression context).
// This usually means a local variable name, but it could also be a type name.

public class VarParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new VarParser();
    private VarParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        Success<String> var = IdentifierParser.singleton.parse(s, p);
        if (var == null)
            return null;
        Variable result = new Variable(var.value);
        return new Success<Expression>(result, var.rem);
    }
}

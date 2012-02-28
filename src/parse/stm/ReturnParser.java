package parse.stm;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import parse.misc.IdentifierParser;
import a.exp.*;
import a.stm.*;

public class ReturnParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new ReturnParser();
    private ReturnParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse the "return".
        Success<String> resReturn = IdentifierParser.singleton.parse(s, p);
        if (resReturn == null || !resReturn.value.equals("return"))
            return null;
        p = resReturn.rem;
        p = optWS(s, p);

        // Parse the expression.
        Success<Expression> resExp = ExpressionParser.singleton.parse(s, p);
        Expression exp = null;
        if (resExp != null) {
            exp = resExp.value;
            p = resExp.rem;
            p = optWS(s, p);
        }

        // Parse the ';'.
        if (s.charAt(p++) != ';') {
            if (resExp == null)
                throw new NiftyException("Missing return value or ';'.");
            throw new NiftyException("Missing ';' at end of return statement.");
        }

        return new Success<Statement>(new Return(exp), p);
    }
}

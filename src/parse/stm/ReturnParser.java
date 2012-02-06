package parse.stm;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import a.exp.Expression;
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
        p = resExp.rem;
        p = optWS(s, p);

        // Parse the ';'.
        if (s.charAt(p++) != ';')
            throw new NiftyException("Missing ';' for return statement.");

        return new Success<Statement>(new Return(resExp.value), p);
    }
}

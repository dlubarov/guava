package parse.exp;

import common.NiftyException;

import a.exp.Expression;
import parse.*;

public class ParentheticalExpressionParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new ParentheticalExpressionParser();
    private ParentheticalExpressionParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        // Parse left paren.
        if (s.charAt(p) != '(')
            return null;
        ++p;
        p = optWS(s, p);

        // Parse inner expression.
        Success<Expression> resExp = ExpressionParser.singleton.parse(s, p);
        p = resExp.rem;
        p = optWS(s, p);

        // Parse right paren.
        if (s.charAt(p) != ')')
            throw new NiftyException("Expecting ')' after %s", resExp.value);
        ++p;

        return new Success<Expression>(resExp.value, p);
    }
}

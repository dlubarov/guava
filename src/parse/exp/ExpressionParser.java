package parse.exp;

import common.NiftyException;

import a.exp.*;
import parse.*;
import parse.exp.bin.IorParser;
import util.StringUtils;

public class ExpressionParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new ExpressionParser().memo();
    private ExpressionParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        // Parse the left hand side of a (possible) assignment.
        Success<Expression> resLeft = IorParser.singleton.parse(s, p);
        if (resLeft == null)
            return null;
        p = resLeft.rem;
        p = optWS(s, p);

        // Try parsing a simple assignment.
        if (s.charAt(p) == '=') {
            ++p;
            p = optWS(s, p);

            Success<Expression> resRight = parse(s, p);
            if (resRight == null)
                throw new NiftyException("Expecting expression following '='.");
            p = resRight.rem;

            return new Success<Expression>(new Assignment(resLeft.value, resRight.value), p);
        }

        // Try parsing the other assignment operators.
        for (String op : OperatorParser.operators) {
            if (!op.endsWith("="))
                continue;
            if (!StringUtils.containsAt(s, op, p))
                continue;
            p += op.length();
            p = optWS(s, p);

            Success<Expression> resRight = parse(s, p);
            if (resRight == null)
                throw new NiftyException("Expecting expression following '%s'.", op);
            p = resRight.rem;

            return new Success<Expression>(new InfixOperation(resLeft.value, op, resRight.value), p);
        }

        // There's no assignment here, just a simpler expression.
        return resLeft;
    }
}

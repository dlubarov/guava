package parse.exp;

import common.NiftyException;

import parse.*;
import util.StringUtils;
import a.exp.*;

public class PrefixExpressionParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new PrefixExpressionParser().memo();
    private PrefixExpressionParser() {}

    private static final String[] prefixOps = {"+", "-", "~", "!"};

    @Override
    public Success<Expression> parse(String s, int p) {
        // Parse the operator.
        String opUsed = null;
        for (String op : prefixOps)
            if (StringUtils.containsAt(s, op, p)) {
                opUsed = op;
                break;
            }
        if (opUsed == null)
            return ChainParser.singleton.parse(s, p);
        p += opUsed.length();
        p = optWS(s, p);

        // Parse the target.
        Success<Expression> resTarget = ChainParser.singleton.parse(s, p);
        if (resTarget == null)
            throw new NiftyException("Expecting expression after unary %s.", opUsed);
        p = resTarget.rem;

        return new Success<Expression>(new PrefixOperation(opUsed, resTarget.value), p);
    }
}

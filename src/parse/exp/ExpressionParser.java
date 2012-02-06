package parse.exp;

import a.exp.Expression;
import parse.*;

public class ExpressionParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new ExpressionParser().memo();
    private ExpressionParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        // TODO Auto-generated method stub
        return null;
    }
}

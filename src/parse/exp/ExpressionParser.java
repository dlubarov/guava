package parse.exp;

import a.exp.*;
import parse.*;

public class ExpressionParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new ExpressionParser().memo();
    private ExpressionParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        return InfixExpressionParser.assignmentParser.parse(s, p);
    }
}

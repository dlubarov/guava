package parse.exp;

import a.exp.Expression;
import parse.*;

public class ExpressionParser extends Parser<Expression> {
    public final ExpressionParser singleton = new ExpressionParser();
    private ExpressionParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        // TODO Auto-generated method stub
        return null;
    }
}

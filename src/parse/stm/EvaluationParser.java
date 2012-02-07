package parse.stm;

import parse.*;
import parse.exp.ExpressionParser;
import a.exp.Expression;
import a.stm.*;

public class EvaluationParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new EvaluationParser();
    private EvaluationParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse the expression.
        Success<Expression> resExp = ExpressionParser.singleton.parse(s, p);
        if (resExp == null)
            return null;
        p = resExp.rem;
        p = optWS(s, p);

        // Parse the ';'.
        if (s.charAt(p++) != ';')
            return null;

        return new Success<Statement>(new Evaluation(resExp.value), p);
    }
}

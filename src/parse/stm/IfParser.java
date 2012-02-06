package parse.stm;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import util.StringUtils;
import a.exp.Expression;
import a.stm.*;

public class IfParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new IfParser();
    private IfParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse "if".
        if (!StringUtils.containsAt(s, "if", p))
            return null;
        p += "if".length();
        boolean spaceAfterIf = optWS(s, p) != p;
        p = optWS(s, p);

        // Parse the '('.
        if (s.charAt(p) != '(') {
            if (spaceAfterIf) // certainly a syntax error
                throw new NiftyException("Missing opening parenthesis after 'if'.");
            return null; // could be valid code, e.g. ifSomethingDoSomething();
        }
        ++p;
        p = optWS(s, p);

        // Parse the condition.
        Success<Expression> resCond = ExpressionParser.singleton.parse(s, p);
        if (resCond == null)
            throw new NiftyException("Expecting expression after 'if ('.");
        p = resCond.rem;
        p = optWS(s, p);

        // Parse the ')'.
        if (s.charAt(p) != ')')
            throw new NiftyException("Missing closing parenthesis after condition expression of if statement.");
        ++p;
        p = optWS(s, p);

        // Parse the body.
        Success<Statement> resBody = StatementParser.singleton.parse(s, p);
        if (resBody == null)
            throw new NiftyException("If statement is missing a body.");
        p = resBody.rem;

        // Try to parse an if-else statement.
        if (StringUtils.containsAt(s, "else", p)) {
            p += "else".length();
            p = optWS(s, p);

            Success<Statement> resElse = StatementParser.singleton.parse(s, p);
            if (resElse == null)
                throw new NiftyException("Expecting statement following 'else'.");
            p = resElse.rem;

            return new Success<Statement>(new IfElse(resCond.value, resBody.value, resElse.value), p);
        }

        // There was no else clause, so this is just an if statement.
        return new Success<Statement>(new If(resCond.value, resBody.value), p);
    }
}

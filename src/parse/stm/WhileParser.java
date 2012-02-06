package parse.stm;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import parse.misc.IdentifierParser;
import a.exp.Expression;
import a.stm.*;

public class WhileParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new WhileParser();
    private WhileParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse the "while".
        Success<String> resWhile = IdentifierParser.singleton.parse(s, p);
        if (resWhile == null || !resWhile.value.equals("while"))
            return null;
        p = resWhile.rem;
        p = optWS(s, p);

        // Parse the '('.
        if (s.charAt(p++) != '(')
            throw new NiftyException("Missing opening parenthesis after 'while'.");
        p = optWS(s, p);

        // Parse the condition.
        Success<Expression> resCond = ExpressionParser.singleton.parse(s, p);
        if (resCond == null)
            throw new NiftyException("Expecting condition expression after 'while ('.");
        p = resCond.rem;
        p = optWS(s, p);

        // Parse the ')'.
        if (s.charAt(p++) != ')')
            throw new NiftyException("Missing closing parenthesis in header of while loop.");
        p = optWS(s, p);

        // Parse the body.
        Success<Statement> resBody = StatementParser.singleton.parse(s, p);
        if (resBody == null)
            throw new NiftyException("Expecting statement in body of while loop.");
        p = resBody.rem;

        return new Success<Statement>(new While(resCond.value, resBody.value), p);
    }
}

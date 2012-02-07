package parse.stm;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import parse.misc.IdentifierParser;
import a.exp.Expression;
import a.stm.*;

public class ForParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new ForParser();
    private ForParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse the "for".
        Success<String> resFor = IdentifierParser.singleton.parse(s, p);
        if (resFor == null || !resFor.value.equals("for"))
            return null;
        p = resFor.rem;
        p = optWS(s, p);

        // Parse the '('.
        if (s.charAt(p++) != '(')
            throw new NiftyException("Missing opening parenthesis after 'for'.");
        p = optWS(s, p);

        // Parse the initial statement.
        Success<Statement> resA = StatementParser.singleton.parse(s, p);
        if (resA == null)
            throw new NiftyException("Missing initial statement in for loop.");
        p = resA.rem;
        p = optWS(s, p);

        // Parse the condition.
        Success<Expression> resB = ExpressionParser.singleton.parse(s, p);
        if (resB == null)
            throw new NiftyException("Missing condition expression in for loop.");
        p = resB.rem;
        p = optWS(s, p);

        // Parse the ';' separating the second and third parts of the header.
        if (s.charAt(p++) != ';')
            throw new NiftyException("Missing ';' after condition expression in for loop.");
        p = optWS(s, p);

        // Parse the other expression.
        Success<Expression> resC = ExpressionParser.singleton.parse(s, p);
        if (resC == null)
            throw new NiftyException("Missing third part of for loop header (should be an expression).");
        p = resC.rem;
        p = optWS(s, p);

        // Parse the ')'.
        if (s.charAt(p++) != ')')
            throw new NiftyException("Missing closing parenthesis in header of for loop.");
        p = optWS(s, p);

        // Parse the body.
        Success<Statement> resBody = StatementParser.singleton.parse(s, p);
        if (resBody == null)
            throw new NiftyException("Expecting statement in body of for loop.");
        p = resBody.rem;

        return new Success<Statement>(new For(resA.value, resB.value, resC.value, resBody.value), p);
    }
}

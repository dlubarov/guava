package parse.stm;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import parse.misc.IdentifierParser;
import a.exp.Expression;
import a.stm.*;

public class RepeatParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new RepeatParser();
    private RepeatParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse the "repeat".
        Success<String> resRepeat = IdentifierParser.singleton.parse(s, p);
        if (resRepeat == null || !resRepeat.value.equals("repeat"))
            return null;
        p = resRepeat.rem;
        p = optWS(s, p);

        // Parse the '('.
        if (s.charAt(p++) != '(')
            throw new NiftyException("Missing opening parenthesis after 'repeat'.");
        p = optWS(s, p);

        // Parse the number.
        Success<Expression> resCond = ExpressionParser.singleton.parse(s, p);
        if (resCond == null)
            throw new NiftyException("Expecting expression after 'repeat ('.");
        p = resCond.rem;
        p = optWS(s, p);

        // Parse the ')'.
        if (s.charAt(p++) != ')')
            throw new NiftyException("Missing closing parenthesis in header of repeat loop.");
        p = optWS(s, p);

        // Parse the body.
        Success<Statement> resBody = StatementParser.singleton.parse(s, p);
        if (resBody == null)
            throw new NiftyException("Expecting statement in body of repeat loop.");
        p = resBody.rem;

        return new Success<Statement>(new Repeat(resCond.value, resBody.value), p);
    }
}

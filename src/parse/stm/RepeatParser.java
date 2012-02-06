package parse.stm;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import util.StringUtils;
import a.exp.Expression;
import a.stm.*;

public class RepeatParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new RepeatParser();
    private RepeatParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse "repeat".
        if (!StringUtils.containsAt(s, "repeat", p))
            return null;
        p += "repeat".length();
        boolean spaceAfterRepeat = optWS(s, p) != p;
        p = optWS(s, p);

        // Parse the '('.
        if (s.charAt(p) != '(') {
            if (spaceAfterRepeat) // certainly a syntax error
                throw new NiftyException("Missing opening parenthesis after 'repeat'.");
            return null; // could be valid code, e.g. repeatedlyBangHead();
        }
        ++p;
        p = optWS(s, p);

        // Parse the number.
        Success<Expression> resCond = ExpressionParser.singleton.parse(s, p);
        if (resCond == null)
            throw new NiftyException("Expecting expression after 'repeat ('.");
        p = resCond.rem;
        p = optWS(s, p);

        // Parse the ')'.
        if (s.charAt(p) != ')')
            throw new NiftyException("Missing closing parenthesis in header of repeat loop.");
        ++p;
        p = optWS(s, p);

        // Parse the body.
        Success<Statement> resBody = StatementParser.singleton.parse(s, p);
        if (resBody == null)
            throw new NiftyException("Expecting statement in body of repeat loop.");
        p = resBody.rem;

        return new Success<Statement>(new Repeat(resCond.value, resBody.value), p);
    }
}

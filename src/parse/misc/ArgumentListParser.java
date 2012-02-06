package parse.misc;

import java.util.*;

import parse.*;
import parse.exp.ExpressionParser;

import common.NiftyException;

import a.exp.Expression;

public class ArgumentListParser extends Parser<Expression[]> {
    public static final Parser<Expression[]> singleton = new ArgumentListParser().memo();
    private ArgumentListParser() {}

    @Override
    public Success<Expression[]> parse(String s, int p) {
        // Parse the '('.
        if (s.charAt(p) != '(')
            return null;
        ++p;
        p = optWS(s, p);

        // Parse the types.
        List<Expression> args = new ArrayList<Expression>();
        for (;;) {
            Success<Expression> resArg = ExpressionParser.singleton.parse(s, p);
            if (resArg == null)
                break;
            args.add(resArg.value);
            p = resArg.rem;
            p = optWS(s, p);
        }

        // Parse the ')'.
        if (s.charAt(p) != ')')
            throw new NiftyException("Expecting ')' after arguments.");
        ++p;

        return new Success<Expression[]>(args.toArray(new Expression[args.size()]), p);
    }
}

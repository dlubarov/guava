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
        if (s.charAt(p++) != '(')
            return null;
        p = optWS(s, p);

        // Handle empty argument lists.
        if (s.charAt(p) == ')')
            return new Success<Expression[]>(Expression.NONE, p + 1);

        // Parse the first argument.
        List<Expression> args = new ArrayList<Expression>();
        Success<Expression> resArg = ExpressionParser.singleton.parse(s, p);
        if (resArg == null)
            return null;
        args.add(resArg.value);
        p = resArg.rem;
        p = optWS(s, p);

        // Parse any remaining arguments.
        for (;;) {
            // Parse the comma.
            if (s.charAt(p) != ',')
                break;
            p = optWS(s, p + 1);

            // Parse the next argument.
            resArg = ExpressionParser.singleton.parse(s, p);
            if (resArg == null)
                throw new NiftyException("Expecting another argument expression after ','.");
            args.add(resArg.value);
            p = resArg.rem;
            p = optWS(s, p);
        }

        // Parse the ')'.
        if (s.charAt(p++) != ')')
            throw new NiftyException("Expecting ')' after arguments.");

        Expression[] result = args.toArray(new Expression[args.size()]);
        return new Success<Expression[]>(result, p);
    }
}

package parse.exp;

import common.NiftyException;

import a.exp.*;
import parse.*;
import parse.misc.*;

public class ChainParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new ChainParser().memo();
    private ChainParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        Success<Expression> resBase = AtomParser.singleton.parse(s, p);
        if (resBase == null)
            return null;
        Expression exp = resBase.value;
        p = resBase.rem;
        p = optWS(s, p);

        Success<GenericsAndArgumentsParser.GenericsAndArgs> resGenericsAndArgs =
                GenericsAndArgumentsParser.singleton.parse(s, p);
        if (resGenericsAndArgs != null) {
            GenericsAndArgumentsParser.GenericsAndArgs val = resGenericsAndArgs.value;
            exp = new Invocation(exp, val.genericArgs, val.arguments);
            p = resGenericsAndArgs.rem;
            p = optWS(s, p);
        }

        for (;;) {
            // Parse the '.'.
            if (s.charAt(p) != '.')
                break;
            p = optWS(s, p + 1);

            // Parse the selector
            Success<String> resMember = IdentifierOrOpParser.singleton.parse(s, p);
            if (resMember == null)
                throw new NiftyException("Expecting member name after '.'.");
            exp = new MemberAccess(exp, resMember.value);
            p = resMember.rem;
            p = optWS(s, p);

            resGenericsAndArgs = GenericsAndArgumentsParser.singleton.parse(s, p);
            if (resGenericsAndArgs != null) {
                GenericsAndArgumentsParser.GenericsAndArgs val = resGenericsAndArgs.value;
                exp = new Invocation(exp, val.genericArgs, val.arguments);
                p = resGenericsAndArgs.rem;
                p = optWS(s, p);
            }
        }

        return new Success<Expression>(exp, p);
    }
}

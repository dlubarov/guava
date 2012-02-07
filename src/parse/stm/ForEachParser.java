package parse.stm;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import parse.misc.*;
import a.Type;
import a.exp.Expression;
import a.stm.*;

public class ForEachParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new ForEachParser();
    private ForEachParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse the "foreach".
        Success<String> resForeach = IdentifierParser.singleton.parse(s, p);
        if (resForeach == null || !resForeach.value.equals("foreach"))
            return null;
        p = resForeach.rem;
        p = optWS(s, p);

        // Parse the '('.
        if (s.charAt(p++) != '(')
            throw new NiftyException("Missing opening parenthesis after 'foreach'.");
        p = optWS(s, p);

        // Parse the type.
        Success<Type> resType = TypeParser.singleton.parse(s, p);
        if (resType == null)
            throw new NiftyException("Expecting type after 'foreach ('.");
        p = resType.rem;
        p = optWS(s, p);

        // Parse the local variable name.
        Success<String> resVar = IdentifierParser.singleton.parse(s, p);
        if (resVar == null)
            throw new NiftyException("Expecting element variable name in foreach.");
        p = resVar.rem;
        p = optWS(s, p);

        // Parse the "in".
        Success<String> resIn = IdentifierParser.singleton.parse(s, p);
        if (resIn == null || !resIn.value.equals("in"))
            throw new NiftyException("Expecting 'in' after variable name in foreach header.");
        p = resIn.rem;
        p = optWS(s, p);

        // Parse the enumerable expression.
        Success<Expression> resEnum = ExpressionParser.singleton.parse(s, p);
        if (resEnum == null)
            throw new NiftyException("Expecting enumerable expression.");
        p = resEnum.rem;
        p = optWS(s, p);

        // Parse the ')'.
        if (s.charAt(p++) != ')')
            throw new NiftyException("Missing closing parenthesis in header of foreach loop.");
        p = optWS(s, p);

        // Parse the body.
        Success<Statement> resBody = StatementParser.singleton.parse(s, p);
        if (resBody == null)
            throw new NiftyException("Expecting statement in body of foreach loop.");
        p = resBody.rem;

        ForEach result = new ForEach(resType.value, resVar.value, resEnum.value, resBody.value);
        return new Success<Statement>(result, p);
    }
}

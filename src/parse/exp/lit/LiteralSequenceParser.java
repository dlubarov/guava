package parse.exp.lit;

import java.util.*;

import common.NiftyException;

import a.exp.*;
import a.exp.lit.LiteralSequence;
import parse.*;
import parse.exp.ExpressionParser;

public class LiteralSequenceParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new LiteralSequenceParser();
    private LiteralSequenceParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        // Parse the '{'.
        if (s.charAt(p++) != '{')
            return null;
        p = optWS(s, p);

        // Handle empty sequences.
        if (s.charAt(p) == '}')
            return new Success<Expression>(new LiteralSequence(Expression.NONE), p + 1);

        // Parse the first element.
        List<Expression> elements = new ArrayList<Expression>();
        Success<Expression> resElem = ExpressionParser.singleton.parse(s, p);
        if (resElem == null)
            throw new NiftyException("Failed to parse first element of literal sequence.");
        elements.add(resElem.value);
        p = resElem.rem;
        p = optWS(s, p);

        // Parse any following elements.
        for (;;) {
            // Parse the comma.
            if (s.charAt(p) != ',')
                break;
            p = optWS(s, p + 1);

            // Parse the next element.
            resElem = ExpressionParser.singleton.parse(s, p);
            if (resElem == null)
                throw new NiftyException("Expecting another expression after ',' in literal sequence.");
            elements.add(resElem.value);
            p = resElem.rem;
            p = optWS(s, p);
        }

        // Parse the '}'.
        if (s.charAt(p++) != '}')
            throw new NiftyException("Could not find end of literal sequence.");

        Expression result = new LiteralSequence(elements.toArray(new Expression[elements.size()]));
        return new Success<Expression>(result, p);
    }
}

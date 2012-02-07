package parse.stm;

import java.util.*;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import parse.misc.*;
import a.Type;
import a.exp.Expression;
import a.stm.*;

public class LocalDefParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new LocalDefParser();
    private LocalDefParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse the type.
        Success<Type> resType = TypeParser.singleton.parse(s, p);
        if (resType == null)
            return null;
        p = resType.rem;
        p = optWS(s, p);

        // Parse each name=expression pair. (Some expressions may be null.)
        List<String> names = new ArrayList<String>();
        List<Expression> initVals = new ArrayList<Expression>();
        for (;;) {
            Success<String> resName = IdentifierParser.singleton.parse(s, p);
            if (resName == null)
                return null;
            names.add(resName.value);
            p = resName.rem;
            p = optWS(s, p);
            if (s.charAt(p) == '=') {
                ++p;
                p = optWS(s, p);
                Success<Expression> resInitVal = ExpressionParser.singleton.parse(s, p);
                if (resInitVal == null)
                    throw new NiftyException("Expecting initial value expression for %s", resName.value);
                initVals.add(resInitVal.value);
                p = resInitVal.rem;
                p = optWS(s, p);
            } else
                initVals.add(null);

            // Parse ';' or ','.
            char c = s.charAt(p++);
            if (c == ';')
                break;
            if (c != ',')
                throw new NiftyException("Expecting ',' or ';'.");
            p = optWS(s, p);
        }

        String[] namesArr = names.toArray(new String[names.size()]);
        Expression[] initValsArr = initVals.toArray(new Expression[initVals.size()]);
        return new Success<Statement>(new LocalDef(resType.value, namesArr, initValsArr), p);
    }
}

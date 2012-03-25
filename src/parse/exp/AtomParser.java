package parse.exp;

import a.exp.*;
import parse.*;
import parse.exp.lit.*;

@SuppressWarnings("unchecked")
public class AtomParser extends Parser<Expression> {
    public static final Parser<Expression> singleton = new AtomParser().memo();
    private AtomParser() {}

    private static final Parser<Expression>[] atomParsers = new Parser[] {
            ParentheticalExpressionParser.singleton,
            LiteralSequenceParser.singleton,
            LiteralDoubleParser.singleton,
            LiteralIntParser.singleton,
            LiteralBoolParser.singleton,
            LiteralCharParser.singleton,
            LiteralStringParser.singleton,
            InstantiationParser.singleton,
            VarParser.singleton
    };

    @Override
    public Success<Expression> parse(String s, int p) {
        // Delegate to the various atomic expression parsers.
        for (Parser<Expression> atomParser : atomParsers) {
            Success<Expression> result = atomParser.parse(s, p);
            if (result != null)
                return result;
        }

        // We tried everything. There is no atomic expression here.
        return null;
    }
}

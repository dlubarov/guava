package parse.stm;

import common.NiftyException;

import parse.*;
import parse.misc.IdentifierParser;
import a.stm.*;

public class ForeverParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new ForeverParser();
    private ForeverParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse "forever".
        Success<String> resForever = IdentifierParser.singleton.parse(s, p);
        if (resForever == null)
            return null;
        if (!resForever.value.equals("forever"))
            return null;
        p = resForever.rem;
        p = optWS(s, p);

        // Parse the body.
        Success<Statement> resBody = StatementParser.singleton.parse(s, p);
        if (resBody == null)
            throw new NiftyException("Expecting statement in body of forever loop.");
        p = resBody.rem;

        return new Success<Statement>(new Forever(resBody.value), p);
    }
}

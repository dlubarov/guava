package parse.exp;

import static util.StringUtils.containsAt;

import common.NiftyException;

import a.Type;
import a.exp.*;
import parse.*;

public class InstantiationParser extends Parser<Expression> {
    public static final InstantiationParser singleton = new InstantiationParser();
    private InstantiationParser() {}

    @Override
    public Success<Expression> parse(String s, int p) {
        // Parse "new".
        if (!containsAt(s, "new", p))
            return null;
        if (optWS(s, p) == p)
            return null; // no space following "new"
        p = optWS(s, p);

        // Parse the type being instantiated.
        Success<Type> resType = TypeParser.singleton.parse(s, p);
        if (resType == null)
            return null;
        p = resType.rem;
        p = optWS(s, p);

        // Parse the argument list.
        Success<Expression[]> resArgList = ArgumentListParser.singleton.parse(s, p);
        if (resArgList == null)
            throw new NiftyException("Missing argument list for new %s.", resType.value);
        p = resArgList.rem;

        return new Success<Expression>(new Instantiation(resType.value, resArgList.value), p);
    }
}

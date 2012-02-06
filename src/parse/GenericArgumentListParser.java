package parse;

import java.util.*;

import common.NiftyException;

import a.Type;

// Parses a generic argument list, such as "[String, Sequence[Int], T]".
// By design, this does NOT permit [] as a valid generic argument list.

public class GenericArgumentListParser extends Parser<Type[]> {
    public static final Parser<Type[]> singleton = new GenericArgumentListParser().memo();

    @Override
    public Success<Type[]> parse(String s, int p) {
        // Parse the '['.
        if (s.charAt(p) != '[')
            return null;
        ++p;
        p = optWS(s, p);

        // Parse the types.
        List<Type> genericArgs = new ArrayList<Type>();
        for (;;) {
            Success<Type> resArg = TypeParser.singleton.parse(s, p);
            if (resArg == null)
                break;
            genericArgs.add(resArg.value);
            p = resArg.rem;
            p = optWS(s, p);
        }

        // Don't permit "[]".
        if (genericArgs.isEmpty())
            throw new NiftyException("Expecting generic arguments after '['.");

        // Parse the ']'.
        if (s.charAt(p) != ']')
            throw new NiftyException("Expecting ']' after generic arguments.");
        ++p;

        return new Success<Type[]>(genericArgs.toArray(new Type[genericArgs.size()]), p);
    }
}

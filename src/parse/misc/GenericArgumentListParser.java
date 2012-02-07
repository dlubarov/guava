package parse.misc;

import java.util.*;

import parse.*;

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

        // Parse the first type.
        List<Type> genericArgs = new ArrayList<Type>();
        Success<Type> resArg = TypeParser.singleton.parse(s, p);
        if (resArg == null) {
            if (s.charAt(p) == ']')
                throw new NiftyException("Empty parameter lists aren't accepted, just don't specify a list.");
            throw new NiftyException("Expecting a type after '[' in generic argument list.");
        }
        genericArgs.add(resArg.value);
        p = resArg.rem;
        p = optWS(s, p);

        // Parse any other types.
        for (;;) {
            // Parse the comma.
            if (s.charAt(p) != ',')
                break;
            p = optWS(s, p + 1);

            // Parse the next type.
            resArg = TypeParser.singleton.parse(s, p);
            if (resArg == null)
                throw new NiftyException("Expecting another type after ',' in generic argument list.");
            genericArgs.add(resArg.value);
            p = resArg.rem;
            p = optWS(s, p);
        }

        // Parse the ']'.
        if (s.charAt(p) != ']')
            throw new NiftyException("Expecting ']' after generic arguments.");
        ++p;

        Type[] result = genericArgs.toArray(new Type[genericArgs.size()]);
        return new Success<Type[]>(result, p);
    }
}

package parse.misc;

import java.util.*;

import common.NiftyException;

import parse.*;

import a.Type;

public class TypeListParser extends Parser<Type[]> {
    public static final Parser<Type[]> singleton = new TypeListParser();
    private TypeListParser() {}

    @Override
    public Success<Type[]> parse(String s, int p) {
        // Parse the '['.
        if (s.charAt(p++) != '[')
            return null;
        p = optWS(s, p);
        if (s.charAt(p) == ']')
            throw new NiftyException("Empty type lists are not accepted; try removing the '[]'.");

        // Parse the first type.
        List<Type> types = new ArrayList<Type>();
        Success<Type> resType = TypeParser.singleton.parse(s, p);
        if (resType == null)
            throw new NiftyException("Expecting at least one type after '['.");
        types.add(resType.value);
        p = resType.rem;
        p = optWS(s, p);

        // Parse any other types.
        for (;;) {
            // Parse the comma.
            if (s.charAt(p) != ',')
                break;
            p = optWS(s, p + 1);

            // Parse the next type.
            resType = TypeParser.singleton.parse(s, p);
            if (resType == null)
                break;
            types.add(resType.value);
            p = resType.rem;
            p = optWS(s, p);
        }

        // Parse the ']'.
        if (s.charAt(p++) != ']')
            throw new NiftyException("Expecting ']' to close type list.");
        Type[] result = types.toArray(new Type[types.size()]);
        return new Success<Type[]>(result, p);
    }
}

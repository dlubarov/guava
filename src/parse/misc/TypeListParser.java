package parse.misc;

import java.util.*;

import common.NiftyException;

import parse.*;

import a.Type;

public class TypeListParser extends Parser<Type[]> {
    public static final TypeListParser singleton = new TypeListParser();
    private TypeListParser() {}

    @Override
    public Success<Type[]> parse(String s, int p) {
        // Parse the '['.
        if (s.charAt(p++) != '[')
            return null;
        p = optWS(s, p);

        // Parse the types.
        List<Type> types = new ArrayList<Type>();
        for (;;) {
            Success<Type> resType = TypeParser.singleton.parse(s, p);
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

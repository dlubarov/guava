package parse.misc;

import java.util.*;

import parse.*;

import a.Type;

public class TypeListParser extends Parser<Type[]> {
    public static final TypeListParser singleton = new TypeListParser();
    private TypeListParser() {}

    @Override
    public Success<Type[]> parse(String s, int p) {
        if (s.charAt(p++) != '[')
            return null;
        List<Type> types = new ArrayList<Type>();
        for (;;) {
            p = optWS(s, p);
            if (s.charAt(p) == ']') {
                ++p;
                Type[] typesArr = types.toArray(new Type[types.size()]);
                return new Success<Type[]>(typesArr, p);
            }
            try {
                Success<Type> succType = TypeParser.singleton.parse(s, p);
                types.add(succType.value);
                p = succType.rem;
            } catch (ClassCastException e) {
                throw new RuntimeException("expecting generic argument");
            }
        }
    }
}

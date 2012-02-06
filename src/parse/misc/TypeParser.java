package parse.misc;

import parse.*;
import a.Type;

public class TypeParser extends Parser<Type> {
    public static final TypeParser singleton = new TypeParser();
    private TypeParser() {}

    @Override
    public Success<Type> parse(String s, int p) {
        Success<String> resRaw = IdentifierParser.singleton.parse(s, p);
        if (resRaw == null)
            return null;
        p = resRaw.rem;
        p = optWS(s, p);
        Success<Type[]> resGenericArgs = TypeListParser.singleton.parse(s, p);
        if (resGenericArgs != null) {
            Type result = new Type(resRaw.value, resGenericArgs.value);
            return new Success<Type>(result, resGenericArgs.rem);
        }
        return new Success<Type>(new Type(resRaw.value), resRaw.rem);
    }
}

package parse.misc;

import parse.*;
import common.Variance;

import a.gen.TypeGenericParam;

public class TypeGenericParamParser extends Parser<TypeGenericParam> {
    public static final Parser<TypeGenericParam> singleton = new TypeGenericParamParser();
    private TypeGenericParamParser() {}

    @Override
    public Success<TypeGenericParam> parse(String s, int p) {
        // Parse the variance.
        Variance var;
        switch (s.charAt(p)) {
            case '+':
                var = Variance.COVARIANT;
                p = optWS(s, p + 1);
                break;
            case '-':
                var = Variance.CONTRAVARIANT;
                p = optWS(s, p + 1);
                break;
            default:
                var = Variance.NONVARIANT;
        }

        // Parse the generic parameter name.
        Success<String> resName = IdentifierParser.singleton.parse(s, p);
        if (resName == null)
            return null;
        p = resName.rem;

        return new Success<TypeGenericParam>(new TypeGenericParam(var, resName.value), p);
    }
}

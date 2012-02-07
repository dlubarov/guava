package parse.misc;

import parse.*;

import a.Type;
import a.exp.Expression;

// Parses an OPTIONAL generic argument list followed by a REQUIRED argument list,
// such as "(2, 'a', false)" or "[String, Int](10)".

public class GenericsAndArgumentsParser extends Parser<GenericsAndArgumentsParser.GenericsAndArgs> {
    public static final Parser<GenericsAndArgs> singleton = new GenericsAndArgumentsParser().memo();

    @Override
    public Success<GenericsAndArgs> parse(String s, int p) {
        // Parse generic arguments. If none are given, use an empty array.
        Type[] genericArgs;
        Success<Type[]> resGenArgs = GenericArgumentListParser.singleton.parse(s, p);
        if (resGenArgs != null) {
            genericArgs = resGenArgs.value;
            p = resGenArgs.rem;
            p = optWS(s, p);
        } else
            genericArgs = Type.NONE;

        // Parse arguments.
        Success<Expression[]> resArgs = ArgumentListParser.singleton.parse(s, p);
        if (resArgs == null)
            return null;
        p = resArgs.rem;

        GenericsAndArgs result = new GenericsAndArgs(genericArgs, resArgs.value);
        return new Success<GenericsAndArgs>(result, p);
    }

    public static class GenericsAndArgs {
        public final Type[] genericArgs;
        public final Expression[] arguments;

        GenericsAndArgs(Type[] genericArgs, Expression[] arguments) {
            this.genericArgs = genericArgs;
            this.arguments = arguments;
        }
    }
}

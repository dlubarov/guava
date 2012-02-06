package parse;

import common.NiftyException;

import a.Type;
import a.exp.Expression;

// Parses an OPTIONAL generic argument list followed by a REQUIRED argument list,
// such as "(2, 'a', false)" or "[String, Int](10)".

public class GenericsAndArgumentsParser extends Parser<GenericsAndArgumentsParser.Result> {
    public static final Parser<Result> singleton = new GenericsAndArgumentsParser().memo();

    @Override
    public Success<Result> parse(String s, int p) {
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
        if (resArgs == null) {
            if (resGenArgs == null)
                return null;
            // Code provides generic args but no args, must be an error.
            throw new NiftyException("Argument list expected.");
        }
        p = resArgs.rem;
        
        return new Success<Result>(new Result(genericArgs, resArgs.value), p);
    }

    public static class Result {
        public final Type[] genericArgs;
        public final Expression[] arguments;

        Result(Type[] genericArgs, Expression[] arguments) {
            this.genericArgs = genericArgs;
            this.arguments = arguments;
        }
    }
}

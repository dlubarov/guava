package parse.stm;

import java.util.*;

import common.NiftyException;

import parse.*;
import parse.exp.ExpressionParser;
import parse.misc.*;
import util.StringUtils;
import a.Type;
import a.exp.*;
import a.stm.*;
import a.stm.Switch.*;

public class SwitchParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new SwitchParser();
    private SwitchParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse the "switch".
        Success<String> resSwitch = IdentifierParser.singleton.parse(s, p);
        if (resSwitch == null || !resSwitch.value.equals("switch"))
            return null;
        p = resSwitch.rem;
        p = optWS(s, p);

        // Parse the '('.
        if (s.charAt(p++) != '(')
            throw new NiftyException("Missing opening parenthesis after 'switch'.");
        p = optWS(s, p);

        // Parse the subject expression.
        Success<Expression> resSubject = ExpressionParser.singleton.parse(s, p);
        if (resSubject == null)
            throw new NiftyException("Expecting subject expression after 'switch ('.");
        p = resSubject.rem;
        p = optWS(s, p);

        // Parse the ')'.
        if (s.charAt(p++) != ')')
            throw new NiftyException("Missing closing parenthesis in header of switch.");
        p = optWS(s, p);

        // Parse the '{'.
        if (s.charAt(p++) != '{')
            throw new NiftyException("Missing '{' in switch statement.");
        p = optWS(s, p);

        List<Pattern> patterns = new ArrayList<Pattern>();
        List<Statement> bodies = new ArrayList<Statement>();

        for (;;) {
            // Parse the pattern.
            Success<Pattern> resPattern = PatternParser.sing.parse(s, p);
            if (resPattern == null)
                break;
            p = resPattern.rem;
            p = optWS(s, p);

            // Parse the "->".
            if (!StringUtils.containsAt(s, "->", p))
                throw new NiftyException("Expecting '->' after pattern in switch statement.");
            p += "->".length();
            p = optWS(s, p);

            // Parse the body.
            Success<Statement> resBody = StatementParser.singleton.parse(s, p);
            if (resBody == null)
                throw new NiftyException("Expecting statement after '->' in switch.");
            p = resBody.rem;
            p = optWS(s, p);

            patterns.add(resPattern.value);
            bodies.add(resBody.value);
        }

        // Parse the '}'.
        if (s.charAt(p++) != '}')
            throw new NiftyException("Expecting a pattern or '}' to end switch statement.");

        // Organize and return the results.
        Pattern[] patternsArr = patterns.toArray(new Pattern[patterns.size()]);
        Statement[] bodiesArr = bodies.toArray(new Statement[bodies.size()]);
        Switch result = new Switch(resSubject.value, patternsArr, bodiesArr);
        return new Success<Statement>(result, p);
    }

    private static class PatternParser extends Parser<Pattern> {
        static final PatternParser sing = new PatternParser();

        @SuppressWarnings("unchecked")
        private static final Parser<Pattern>[] patternParsers = new Parser[] {
            OtherPatternParser.sing,
            TypePatternParser.sing,
            ValuePatternParser.sing
        };

        @Override
        public Success<Pattern> parse(String s, int p) {
            // Try each pattern type.
            for (Parser<Pattern> patternParser : patternParsers) {
                Success<Pattern> result = patternParser.parse(s, p);
                if (result != null)
                    return result;
            }

            // Did not get a successful parse for any pattern type.
            // This must not be a pattern.
            return null;
        }
    }

    private static class ValuePatternParser extends Parser<Pattern> {
        static final ValuePatternParser sing = new ValuePatternParser();

        @Override
        public Success<Pattern> parse(String s, int p) {
            Success<Expression> result = ExpressionParser.singleton.parse(s, p);
            if (result == null)
                return null;
            return new Success<Pattern>(new ValuePattern(result.value), result.rem);
        }
    }

    private static class TypePatternParser extends Parser<Pattern> {
        static final TypePatternParser sing = new TypePatternParser();

        @Override
        public Success<Pattern> parse(String s, int p) {
            // Parse the type.
            Success<Type> resType = TypeParser.singleton.parse(s, p);
            if (resType == null)
                return null;
            p = resType.rem;
            p = optWS(s, p);

            // Parse the local name.
            Success<String> resLocal = IdentifierParser.singleton.parse(s, p);
            if (resLocal == null)
                return null;
            p = resLocal.rem;

            Pattern result = new TypePattern(resType.value, resLocal.value);
            return new Success<Pattern>(result, p);
        }
    }

    private static class OtherPatternParser extends Parser<Pattern> {
        static final OtherPatternParser sing = new OtherPatternParser();

        @Override
        public Success<Pattern> parse(String s, int p) {
            Success<String> result = IdentifierParser.singleton.parse(s, p);
            if (result != null && result.value.equals("other"))
                return new Success<Pattern>(OtherPattern.singleton, result.rem);
            return null;
        }
    }
}

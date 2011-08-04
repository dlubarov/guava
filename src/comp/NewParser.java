package comp;

/*import ast.*;
import ast.exp.*;
import ast.stm.*;

import java.util.*;

public final class NewParser {
    private final static Set<String> operators;

    static {
        operators = new HashSet<String>();
        for (InfixOp op : InfixOp.values())
            operators.add(op.toString());
        for (PrefixOp op : PrefixOp.values())
            operators.add(op.toString());
    }

    private final CharSequence source;

    public NewParser(CharSequence source) {
        this.source = source;
    }

    private Character next(int p) {
        try {
            return source.charAt(p);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private boolean parseChar(int p, char c) {
        try {
            if (source.charAt(p) == c)
                return true;
        } catch (IndexOutOfBoundsException e) {}
        return false;
    }

    private Integer parseString(int p, String s) {
        int l = s.length();
        try {
            if (source.subSequence(p, p + l).toString().equals(s))
                return p + l;
        } catch (IndexOutOfBoundsException e) {}
        return null;
    }

    private int optWS(int p) {
        try {
            while (Character.isWhitespace(source.charAt(p)))
                ++p;
        } catch (IndexOutOfBoundsException e) {}
        return p;
    }

    private ParseResult<String> parseIdentifier(int p) {
        Character first = next(p);
        if (first == null || !Character.isJavaIdentifierStart(first))
            return new Failure("expecting identifier");
        StringBuilder sb = new StringBuilder().append(first);
        try {
            while (Character.isJavaIdentifierPart(source.charAt(p)))
                sb.append(source.charAt(p++));
        } catch (IndexOutOfBoundsException e) {}
        return new Success<String>(sb.toString(), p);
    }

    private ParseResult<String> parseIdentifierOrOperator(int p) {
        ParseResult<String> resIdent = parseIdentifier(p);
        if (resIdent instanceof Success)
            return resIdent;
        for (String op : operators) {
            Integer res = parseString(p, op);
            if (res != null)
                return new Success<String>(op, res);
        }
        return new Failure("expecting identifier or operator");
    }

    private ParseResult<LitBool> parseLitBool(int p) {
        Integer res;
        res = parseString(p, "true");
        if (res != null)
            return new Success<LitBool>(LitBool.TRUE, res);
        res = parseString(p, "false");
        if (res != null)
            return new Success<LitBool>(LitBool.FALSE, res);
        return new Failure("expecting bool");
    }

    private ParseResult<LitInt> parseLitInt(int p) {
        // TODO: hex, octal, binary
        Character first = next(p);
        if (first == null || !Character.isDigit(first))
            return new Failure("expecting int");
        StringBuilder sb = new StringBuilder().append(first);
        try {
            while (Character.isDigit(source.charAt(p)))
                sb.append(source.charAt(p++));
        } catch (IndexOutOfBoundsException e) {}
        return new Success<LitInt>(new LitInt(Integer.parseInt(sb.toString())), p);
    }

    private ParseResult<Type> parseType(int p) {
        ParseResult<String> resName = parseIdentifier(p);
        if (resName instanceof Failure)
            return (Failure) resName;
        Success<String> succName = (Success<String>) resName;
        p = succName.rem;
        p = optWS(p);
        if (parseChar(p++, '[')) {
            p = optWS(p);
            Success<Type[]> succGenerics = parseTypes(p);
            p = succGenerics.rem;
            p = optWS(p);
            if (!parseChar(p++, ']'))
                return new Failure("expecting ']' to end generic argument list");
            return new Success<Type>(new Type(succName.val, succGenerics.val), p);
        }
        return new Success<Type>(new Type(succName.val, new Type[0]), succName.rem);
    }

    private Success<Type[]> parseTypes(int p) {
        List<Type> parts = new ArrayList<Type>();
        for (;;) {
            int p2 = p;
            if (!parts.isEmpty()) {
                p2 = optWS(p);
                if (!parseChar(p2, ','))
                    break;
                p2 = optWS(p2);
            }
            ParseResult<Type> res = parseType(p2);
            if (res instanceof Failure)
                break;
            Success<Type> succ = (Success<Type>) res;
            parts.add(succ.val); p = succ.rem;
        }
        return new Success<Type[]>(parts.toArray(new Type[0]), p);
    }

    private ParseResult<TypedVar> parseTypedVar(int p) {
        ;
    }

    private ParseResult<TypedVar> parseTypedVarAllowOps(int p) {
        ;
    }

    private ParseResult<Expression> parseExpression(int p) {
        need to impl
        return (ParseResult) parseLitInt(p);
    }

    private ParseResult<BlockStm> parseBlockStm(int p) {
        ;
    }

    private ParseResult<IfElseStm> parseIfElseStm(int p) {
        ;
    }
}

abstract class ParseResult<T> {}

class Success<T> extends ParseResult<T> {
    final T val;
    final int rem;

    public Success(T val, int rem) {
        this.val = val;
        this.rem = rem;
    }
}

class Failure extends ParseResult {
    private final String message;

    Failure() {
        this.message = "(no message)";
    }

    Failure(String message) {
        this.message = message;
    }

    Failure(String format, Object... args) {
        this.message = String.format(format, args);
    }

    public String toString() {
        return "Parse error: " + message;
    }
}
*/
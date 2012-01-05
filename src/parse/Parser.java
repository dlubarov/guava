package parse;

public abstract class Parser<T> {
    public abstract Success<T> parse(String s, int p);

    protected int optWS(String s, int p) {
        while (Character.isWhitespace(s.charAt(p)))
            ++p;
        return p;
    }
}

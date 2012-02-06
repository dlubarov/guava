package parse;

public abstract class Parser<T> {
    public abstract Success<T> parse(String s, int p);

    // Return a memoization wrapper around this parser.
    public Parser<T> memo() {
        return new MemoParser<T>(this);
    }

    protected int optWS(String s, int p) {
        while (p < s.length()) {
            char c = s.charAt(p);
            if (Character.isWhitespace(c))
                ++p;
            else if (c == '#')
                while (p < s.length() && s.charAt(p) != '\n')
                    ++p;
            else break;
        }
        return p;
    }
}

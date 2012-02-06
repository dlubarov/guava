package parse;

import java.util.*;

public class MemoParser<T> extends Parser<T> {
    private final Parser<T> inner;
    private final Map<Integer, Map<Integer, Success<T>>> mem;

    public MemoParser(Parser<T> inner) {
        this.inner = inner;
        mem = new HashMap<Integer, Map<Integer, Success<T>>>();
    }

    @Override
    public Success<T> parse(String s, int p) {
        int sID = System.identityHashCode(s); // String hash would be too slow
        Map<Integer, Success<T>> sMem = mem.get(sID);
        if (sMem == null)
            mem.put(sID, sMem = new HashMap<Integer, Success<T>>());
        Success<T> result = sMem.get(p);
        if (result == null)
            sMem.put(p, result = inner.parse(s, p));
        return result;
    }

    @Override
    public Parser<T> memo() {
        // In case a parser is accidentally wrapped in multiple MemoParsers.
        // Could alternatively throw an exception, since this shouldn't happen.
        return this;
    }
}

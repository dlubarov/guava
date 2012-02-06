package parse.stm;

import parse.*;
import a.stm.*;

public class PassParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new PassParser();
    private PassParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        if (s.charAt(p) == ';')
            return new Success<Statement>(Pass.singleton, p + 1);
        return null;
    }
}

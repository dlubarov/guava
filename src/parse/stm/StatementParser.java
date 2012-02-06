package parse.stm;

import parse.*;
import a.stm.Statement;

public class StatementParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new StatementParser().memo();
    private StatementParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // FIXME Auto-generated method stub
        return null;
    }
}

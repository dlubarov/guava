package parse.stm;

import parse.*;
import a.stm.Statement;

public class StatementParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new StatementParser().memo();
    private StatementParser() {}

    @SuppressWarnings("unchecked")
    private static final Parser<Statement>[] statementParsers = new Parser[] {
        BlockParser.singleton,
        PassParser.singleton,
        IfParser.singleton,
        WhileParser.singleton,
        ForParser.singleton,
        ForEachParser.singleton,
        SwitchParser.singleton,
        RepeatParser.singleton,
        ForeverParser.singleton,
        ReturnParser.singleton,
        EvaluationParser.singleton,
        LocalDefParser.singleton,
    };

    @Override
    public Success<Statement> parse(String s, int p) {
        for (Parser<Statement> stmParser : statementParsers) {
            Success<Statement> result = stmParser.parse(s, p);
            if (result != null)
                return result;
        }

        // We tried everything. This isn't a statement.
        return null;
    }
}

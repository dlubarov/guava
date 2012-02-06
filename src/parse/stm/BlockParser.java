package parse.stm;

import java.util.*;

import common.NiftyException;

import parse.*;
import a.stm.*;

public class BlockParser extends Parser<Statement> {
    public static final Parser<Statement> singleton = new BlockParser();
    private BlockParser() {}

    @Override
    public Success<Statement> parse(String s, int p) {
        // Parse "{".
        if (s.charAt(p) != '{')
            return null;
        ++p;
        p = optWS(s, p);

        // Parse statements.
        List<Statement> parts = new ArrayList<Statement>();
        for (;;) {
            Success<Statement> resPart = StatementParser.singleton.parse(s, p);
            if (resPart == null)
                break;
            parts.add(resPart.value);
            p = resPart.rem;
            p = optWS(s, p);
        }

        // Parse "}".
        if (s.charAt(p) != '}')
            throw new NiftyException("Missing closing '}' in block statement.");
        ++p;

        Statement[] partsArr = parts.toArray(new Statement[parts.size()]);
        return new Success<Statement>(new Block(partsArr), p);
    }
}

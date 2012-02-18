package parse.stm;

import java.util.*;

import common.NiftyException;

import parse.*;
import a.stm.*;

public class BlockParser extends Parser<Block> {
    public static final Parser<Block> singleton = new BlockParser();
    private BlockParser() {}

    @Override
    public Success<Block> parse(String s, int p) {
        // Parse "{".
        if (s.charAt(p++) != '{')
            return null;
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
        if (s.charAt(p++) != '}')
            throw new NiftyException("Expecting statement or '}' in block.");

        Statement[] partsArr = parts.toArray(new Statement[parts.size()]);
        return new Success<Block>(new Block(partsArr), p);
    }
}

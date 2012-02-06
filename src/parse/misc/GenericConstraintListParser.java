package parse.misc;

import java.util.*;

import common.NiftyException;

import a.gen.GenericConstraint;
import parse.*;

// An OPTIONAL list of generic constraints. If no [...] is found, the parse
// will SUCCEED and simply return an empty array.

public class GenericConstraintListParser extends Parser<GenericConstraint[]> {
    public static final Parser<GenericConstraint[]> singleton = new GenericConstraintListParser();
    private GenericConstraintListParser() {}

    @Override
    public Success<GenericConstraint[]> parse(String s, int p) {
        List<GenericConstraint> constraints = new ArrayList<GenericConstraint>();
        if (s.charAt(p) == '[') {
            p = optWS(s, p + 1);
            for (;;) {
                Success<GenericConstraint> resConstraint = GenericConstraintParser.singleton.parse(s, p);
                if (resConstraint == null)
                    break;
                constraints.add(resConstraint.value);
                p = resConstraint.rem;
                p = optWS(s, p);
            }
            if (s.charAt(p++) != ']')
                throw new NiftyException("Missing closing ']' after generic constraint list.");
            if (constraints.isEmpty())
                throw new NiftyException("List of generic constraints is empty.");
            p = optWS(s, p);
        }
        GenericConstraint[] result = constraints.toArray(new GenericConstraint[constraints.size()]);
        return new Success<GenericConstraint[]>(result, p);
    }
}

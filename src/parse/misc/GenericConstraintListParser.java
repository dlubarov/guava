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

            // Parse the first constraint.
            Success<GenericConstraint> resConstraint = GenericConstraintParser.singleton.parse(s, p);
            if (resConstraint == null)
                throw new NiftyException("Expecting at least one generic constraint after '['.");
            constraints.add(resConstraint.value);
            p = resConstraint.rem;
            p = optWS(s, p);

            // Parse any other constraints.
            for (;;) {
                // Parse the comma.
                if (s.charAt(p) != ',')
                    break;
                p = optWS(s, p + 1);

                // Parse the next constraint.
                resConstraint = GenericConstraintParser.singleton.parse(s, p);
                if (resConstraint == null)
                    throw new NiftyException("Expecting another generic constraint after ','.");
                constraints.add(resConstraint.value);
                p = resConstraint.rem;
                p = optWS(s, p);
            }

            // Parse the ']'.
            if (s.charAt(p++) != ']')
                throw new NiftyException("Missing closing ']' after generic constraint list.");
            p = optWS(s, p);
        }

        GenericConstraint[] result = constraints.toArray(new GenericConstraint[constraints.size()]);
        return new Success<GenericConstraint[]>(result, p);
    }
}

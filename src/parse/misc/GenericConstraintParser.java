package parse.misc;

import parse.*;
import common.*;

import a.Type;
import a.gen.*;

public class GenericConstraintParser extends Parser<GenericConstraint> {
    public static final Parser<GenericConstraint> singleton = new GenericConstraintParser();
    private GenericConstraintParser() {}

    @Override
    public Success<GenericConstraint> parse(String s, int p) {
        // Parse the generic parameter name.
        Success<String> resGenParam = IdentifierParser.singleton.parse(s, p);
        if (resGenParam == null)
            return null;
        p = resGenParam.rem;
        p = optWS(s, p);

        // Parse the relationship.
        GenericConstraintRel rel;
        char c = s.charAt(p++);
        if (c == '<')
            rel = GenericConstraintRel.SUBTYPE;
        else if (c == '>')
            rel = GenericConstraintRel.SUPERTYPE;
        else
            throw new NiftyException("Expecting '<' or '>' in generic constraint; found '%c'.", c);
        p = optWS(s, p);

        // Parse the other type.
        Success<Type> resType = TypeParser.singleton.parse(s, p);
        p = resType.rem;

        GenericConstraint result = new GenericConstraint(resGenParam.value, rel, resType.value);
        return new Success<GenericConstraint>(result, p);
    }
}

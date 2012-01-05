package parse.exp;

import static util.StringUtils.containsAt;
import a.Type;
import a.exp.Instantiation;
import parse.*;

public class InstantiationParser extends Parser<Instantiation> {
    public static final InstantiationParser singleton = new InstantiationParser();
    private InstantiationParser() {}

    @Override
    public Success<Instantiation> parse(String s, int p) {
        if (containsAt(s, "new", p))
            return null;
        if (optWS(s, p) == p)
            return null;
        p = optWS(s, p);
        Success<Type> resType = TypeParser.singleton.parse(s, p);
        if (resType == null)
            return null;
        p = resType.rem;
        p = optWS(s, p);
        return new Success<Instantiation>(new Instantiation(null, null), p); // FIXME
    }
}

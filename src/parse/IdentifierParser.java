package parse;

public class IdentifierParser extends Parser<String> {
    public static final IdentifierParser singleton = new IdentifierParser();
    private IdentifierParser() {}

    @Override
    public Success<String> parse(String s, int p) {
        if (!Character.isJavaIdentifierStart(s.charAt(p)))
            return null;
        StringBuilder sb = new StringBuilder(Character.toString(s.charAt(p++)));
        while (Character.isJavaIdentifierPart(s.charAt(p)))
            sb.append(s.charAt(p++));
        return new Success<String>(sb.toString(), p);
    }
}
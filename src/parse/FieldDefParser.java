package parse;

import a.*;

public class FieldDefParser extends Parser<MemberDef> {
    public static final Parser<MemberDef> singleton = new FieldDefParser();
    private FieldDefParser() {}

    @Override
    public Success<MemberDef> parse(String s, int p) {
        throw new RuntimeException("FIXME impl"); // FIXME
    }
}

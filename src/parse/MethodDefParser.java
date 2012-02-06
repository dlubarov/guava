package parse;

import a.*;

public class MethodDefParser extends Parser<MemberDef> {
    public static final Parser<MemberDef> singleton = new MethodDefParser();
    private MethodDefParser() {}

    @Override
    public Success<MemberDef> parse(String s, int p) {
        throw new RuntimeException("FIXME impl"); // FIXME
    }
}
